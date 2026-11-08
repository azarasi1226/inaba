package jp.inaba.service2.feature.command.order.releasestock

import jp.inaba.core.domain.order.OrderId
import jp.inaba.message.CommandResult
import jp.inaba.message.InabaEventTag
import jp.inaba.message.order.command.ReleaseOrderStockCommand
import jp.inaba.message.order.command.ReleaseOrderStockResult
import jp.inaba.message.order.event.StockConfirmedEvent
import jp.inaba.message.order.event.StockReservationReleasedEvent
import jp.inaba.message.order.event.StockReservedEvent
import org.axonframework.eventsourcing.annotation.EventSourcingHandler
import org.axonframework.eventsourcing.annotation.reflection.EntityCreator
import org.axonframework.extension.spring.stereotype.EventSourced
import org.axonframework.messaging.commandhandling.annotation.CommandHandler
import org.axonframework.messaging.eventhandling.gateway.EventAppender
import org.axonframework.modelling.annotation.InjectEntity
import org.springframework.stereotype.Component

/**
 * 確保していた在庫を解放する補償ハンドラ(OrderSaga 図の「確保していた在庫を解放」)。
 *
 * 確保イベントが order_id タグを持つので、注文ストリーム 1 本だけ読めば
 * 「今この注文がいくつ確保中か」が分かる。境界が単一タグで済むのがポイント。
 */
@Component
class ReleaseOrderStockCommandHandler {
    @CommandHandler
    fun handle(
        command: ReleaseOrderStockCommand,
        @InjectEntity state: State,
        eventAppender: EventAppender,
    ): CommandResult {
        // 確保が残っていなければ何もしない(冪等: 二重解放・確保なしリトライを吸収)
        if (state.activeReservations.isEmpty()) {
            return ReleaseOrderStockResult.success()
        }

        eventAppender.append(
            *state.activeReservations
                .map { (productId, quantity) ->
                    StockReservationReleasedEvent(
                        productId = productId,
                        orderId = command.orderId.value,
                        quantity = quantity,
                    )
                }.toTypedArray(),
        )

        return ReleaseOrderStockResult.success()
    }

    @EventSourced(tagKey = InabaEventTag.ORDER_ID, idType = OrderId::class)
    class State(
        // productId -> まだ解放されていない確保数
        val activeReservations: MutableMap<String, Int>,
    ) {
        @EntityCreator
        constructor() : this(activeReservations = mutableMapOf())

        @EventSourcingHandler
        fun evolve(event: StockReservedEvent) {
            activeReservations.merge(event.productId, event.quantity, Int::plus)
        }

        @EventSourcingHandler
        fun evolve(event: StockReservationReleasedEvent) {
            removeReservation(event.productId, event.quantity)
        }

        @EventSourcingHandler
        fun evolve(event: StockConfirmedEvent) {
            // 確定済みの分はもう解放できない(出荷済み相当)。確保残から消し込む。
            removeReservation(event.productId, event.confirmedQuantity)
        }

        private fun removeReservation(
            productId: String,
            quantity: Int,
        ) {
            val remaining = (activeReservations[productId] ?: 0) - quantity
            if (remaining <= 0) {
                activeReservations.remove(productId)
            } else {
                activeReservations[productId] = remaining
            }
        }
    }
}
