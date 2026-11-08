package jp.inaba.service2.feature.command.order.confirmstock

import jp.inaba.core.domain.product.DecreaseStockQuantity
import jp.inaba.core.domain.product.StockQuantity
import jp.inaba.message.CommandResult
import jp.inaba.message.InabaEventTag
import jp.inaba.message.order.command.ConfirmOrderStockCommand
import jp.inaba.message.order.command.ConfirmOrderStockResult
import jp.inaba.message.order.event.StockConfirmedEvent
import jp.inaba.message.order.event.StockReservationReleasedEvent
import jp.inaba.message.order.event.StockReservedEvent
import jp.inaba.message.product.event.ProductCreatedEvent
import jp.inaba.message.product.event.StockDecreasedEvent
import jp.inaba.message.product.event.StockIncreasedEvent
import org.axonframework.eventsourcing.annotation.EventCriteriaBuilder
import org.axonframework.eventsourcing.annotation.EventSourcingHandler
import org.axonframework.eventsourcing.annotation.reflection.EntityCreator
import org.axonframework.extension.spring.stereotype.EventSourced
import org.axonframework.messaging.commandhandling.annotation.CommandHandler
import org.axonframework.messaging.eventhandling.gateway.EventAppender
import org.axonframework.messaging.eventstreaming.EventCriteria
import org.axonframework.messaging.eventstreaming.Tag
import org.axonframework.modelling.annotation.InjectEntity
import org.springframework.stereotype.Component
import kotlin.reflect.jvm.jvmName

/**
 * 確保していた在庫を確定(物理在庫を減算)するハンドラ。OrderSaga の最終段。
 *
 * 確保(Reserve)と対称の多ストリーム DCB:
 *  - order ストリーム  : この注文の確保残([State.reserved])と確定済み([State.confirmed])を復元
 *  - product ストリーム: 確定後の在庫を計算するための現在庫([State.onHand])を復元
 * append-condition により、読んだ各 product ストリームに割り込みが無いことが保証される。
 */
@Component
class ConfirmOrderStockCommandHandler {
    @CommandHandler
    fun handle(
        command: ConfirmOrderStockCommand,
        @InjectEntity state: State,
        eventAppender: EventAppender,
    ): CommandResult {
        // ① 冪等性: 既に確定済みなら成功で返す(SagaHandler 再送に耐える)
        if (state.confirmed) {
            return ConfirmOrderStockResult.success()
        }

        // ② 確定対象の確保が残っていなければ失敗(解放済み・確保なし)
        val activeReservations = state.reserved.filterValues { it > 0 }
        if (activeReservations.isEmpty()) {
            return ConfirmOrderStockResult.noReservation()
        }

        // ③ 各商品の現在庫から確保分を引けるか検証(案B では通常通るが、
        //    直接 DecreaseStock パスと競合した場合の保険。append-condition と二重で守る)
        val insufficient =
            activeReservations
                .filter { (productId, quantity) ->
                    StockQuantity(state.onHand[productId] ?: 0)
                        .canNotSubtract(DecreaseStockQuantity(quantity))
                }.map { it.key }
        if (insufficient.isNotEmpty()) {
            return ConfirmOrderStockResult.insufficientStock(insufficient)
        }

        // ④ 確保分を物理在庫から確定減算。確保→確定で onHand が初めて減る。
        eventAppender.append(
            *activeReservations
                .map { (productId, quantity) ->
                    val decreased =
                        StockQuantity(state.onHand[productId] ?: 0)
                            .subtract(DecreaseStockQuantity(quantity))
                    StockConfirmedEvent(
                        productId = productId,
                        orderId = command.orderId.value,
                        confirmedQuantity = quantity,
                        decreasedStockQuantity = decreased.value,
                    )
                }.toTypedArray(),
        )

        return ConfirmOrderStockResult.success()
    }

    @EventSourced(idType = ConfirmOrderStockCommand.TargetId::class)
    class State(
        // productId -> 現在の物理在庫(product ストリームから)
        val onHand: MutableMap<String, Int>,
        // productId -> この注文の確保残(order ストリームから。確定/解放で消し込む)
        val reserved: MutableMap<String, Int>,
        // この注文が確定済みか(冪等性)
        var confirmed: Boolean,
    ) {
        companion object {
            @JvmStatic
            @EventCriteriaBuilder
            private fun resolveCriteria(id: ConfirmOrderStockCommand.TargetId): EventCriteria {
                val productCriteria =
                    id.productIds.map { productId ->
                        EventCriteria
                            .havingTags(Tag.of(InabaEventTag.PRODUCT_ID, productId.value))
                            .andBeingOneOfTypes(
                                ProductCreatedEvent::class.jvmName,
                                StockIncreasedEvent::class.jvmName,
                                StockDecreasedEvent::class.jvmName,
                                StockConfirmedEvent::class.jvmName,
                            )
                    }

                val orderCriteria =
                    EventCriteria
                        .havingTags(Tag.of(InabaEventTag.ORDER_ID, id.orderId.value))
                        .andBeingOneOfTypes(
                            StockReservedEvent::class.jvmName,
                            StockReservationReleasedEvent::class.jvmName,
                            StockConfirmedEvent::class.jvmName,
                        )

                return EventCriteria.either(*(productCriteria + orderCriteria).toTypedArray())
            }
        }

        @EntityCreator
        constructor() : this(
            onHand = mutableMapOf(),
            reserved = mutableMapOf(),
            confirmed = false,
        )

        @EventSourcingHandler
        fun evolve(event: ProductCreatedEvent) {
            onHand[event.id] = event.quantity
        }

        @EventSourcingHandler
        fun evolve(event: StockIncreasedEvent) {
            onHand[event.id] = event.increasedStockQuantity
        }

        @EventSourcingHandler
        fun evolve(event: StockDecreasedEvent) {
            onHand[event.id] = event.decreasedStockQuantity
        }

        @EventSourcingHandler
        fun evolve(event: StockReservedEvent) {
            reserved.merge(event.productId, event.quantity, Int::plus)
        }

        @EventSourcingHandler
        fun evolve(event: StockReservationReleasedEvent) {
            reserved.merge(event.productId, -event.quantity, Int::plus)
        }

        @EventSourcingHandler
        fun evolve(event: StockConfirmedEvent) {
            // 確定で物理在庫が減り、確保残も消える
            onHand[event.productId] = event.decreasedStockQuantity
            reserved.merge(event.productId, -event.confirmedQuantity, Int::plus)
            confirmed = true
        }
    }
}
