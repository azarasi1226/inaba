package jp.inaba.service2.feature.command.order.reservestock

import jp.inaba.core.domain.order.OrderId
import jp.inaba.core.domain.product.ProductId
import jp.inaba.message.CommandResult
import jp.inaba.message.InabaEventTag
import jp.inaba.message.order.command.ReserveOrderStockCommand
import jp.inaba.message.order.command.ReserveOrderStockResult
import jp.inaba.message.order.event.StockConfirmedEvent
import jp.inaba.message.order.event.StockReservationReleasedEvent
import jp.inaba.message.order.event.StockReservedEvent
import jp.inaba.message.product.event.ProductCreatedEvent
import jp.inaba.message.product.event.ProductDeletedEvent
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
 * オーダー内の全商品の在庫を all-or-nothing で確保するハンドラ。これが DCB の本体。
 *
 * 既存の [jp.inaba.service2.feature.command.product.decreasestock.DecreaseStockCommandHandler] は
 * product 1 本の楽観ロックだったが、ここでは [State.resolveCriteria] が
 * 注文明細の商品数だけ EventCriteria を動的生成して 1 つの整合性境界に束ねる。
 */
@Component
class ReserveOrderStockCommandHandler {
    @CommandHandler
    fun handle(
        command: ReserveOrderStockCommand,
        @InjectEntity state: State,
        eventAppender: EventAppender,
    ): CommandResult {
        // ① 冪等性: この注文の確保が既に済んでいるなら成功で返す。
        //    SagaHandler の再送やリトライで二重確保しないための歯止め。
        if (state.alreadyReservedBy(command.orderId)) {
            return ReserveOrderStockResult.success()
        }

        // ② 存在チェック(商品をまたいで)
        val missing =
            command.lines
                .map { it.productId.value }
                .filter { it !in state.existingProductIds }
        if (missing.isNotEmpty()) {
            return ReserveOrderStockResult.productNotFound(missing)
        }

        // ③ all-or-nothing 判定。1 つでも不足していたら注文ごと失敗。
        //    available = 物理在庫 - 確保済み(他注文ぶんを含む)
        val shortage =
            command.lines
                .filter { state.available(it.productId) < it.quantity.value }
                .map { it.productId.value }
        if (shortage.isNotEmpty()) {
            return ReserveOrderStockResult.outOfStock(shortage)
        }

        // ④ 全明細を 1 まとまりで append。
        //    ★DCB の肝★ Axon はこの append に対して
        //    「②③で読んだ各 product ストリームに、その後 新イベントが割り込んでいないか」を
        //    append-condition(楽観ロック)として検証する。
        //    並行注文が横から在庫を減らしていたらここで衝突して弾かれる → 二重売り防止。
        eventAppender.append(
            *command.lines
                .map { line ->
                    StockReservedEvent(
                        productId = line.productId.value,
                        orderId = command.orderId.value,
                        quantity = line.quantity.value,
                    )
                }.toTypedArray(),
        )

        return ReserveOrderStockResult.success()
    }

    @EventSourced(idType = ReserveOrderStockCommand.TargetId::class)
    class State(
        // productId -> 物理在庫
        val onHand: MutableMap<String, Int>,
        // productId -> 確保済み合計(全注文ぶん)
        val reserved: MutableMap<String, Int>,
        val existingProductIds: MutableSet<String>,
        // この注文がこれまでに確保した注文 ID(冪等性用)
        val reservedOrderIds: MutableSet<String>,
    ) {
        companion object {
            @JvmStatic
            @EventCriteriaBuilder
            private fun resolveCriteria(id: ReserveOrderStockCommand.TargetId): EventCriteria {
                // ★商品の数だけ criteria を動的生成 = 動的な整合性境界(DCB)
                val productCriteria =
                    id.productIds.map { productId ->
                        EventCriteria
                            .havingTags(Tag.of(InabaEventTag.PRODUCT_ID, productId.value))
                            .andBeingOneOfTypes(
                                ProductCreatedEvent::class.jvmName,
                                ProductDeletedEvent::class.jvmName,
                                StockIncreasedEvent::class.jvmName,
                                StockDecreasedEvent::class.jvmName,
                                StockReservedEvent::class.jvmName,
                                StockReservationReleasedEvent::class.jvmName,
                                StockConfirmedEvent::class.jvmName,
                            )
                    }

                // 注文ストリーム: この注文の確保/解放/確定を引いて冪等判定する
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
            existingProductIds = mutableSetOf(),
            reservedOrderIds = mutableSetOf(),
        )

        fun available(productId: ProductId): Int =
            (onHand[productId.value] ?: 0) - (reserved[productId.value] ?: 0)

        fun alreadyReservedBy(orderId: OrderId): Boolean = orderId.value in reservedOrderIds

        @EventSourcingHandler
        fun evolve(event: ProductCreatedEvent) {
            existingProductIds.add(event.id)
            onHand[event.id] = event.quantity
        }

        @EventSourcingHandler
        fun evolve(event: ProductDeletedEvent) {
            existingProductIds.remove(event.id)
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
            reservedOrderIds.add(event.orderId)
        }

        @EventSourcingHandler
        fun evolve(event: StockReservationReleasedEvent) {
            reserved.merge(event.productId, -event.quantity, Int::plus)
            reservedOrderIds.remove(event.orderId)
        }

        @EventSourcingHandler
        fun evolve(event: StockConfirmedEvent) {
            // 確定で物理在庫が減り、確保残も消える。
            // reservedOrderIds は残す(確定済み注文の再確保を冪等に弾くため)。
            onHand[event.productId] = event.decreasedStockQuantity
            reserved.merge(event.productId, -event.confirmedQuantity, Int::plus)
        }
    }
}
