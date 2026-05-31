package jp.inaba.service2.feature.command.product.decreasestock

import jp.inaba.core.domain.common.IdempotenceChecker
import jp.inaba.core.domain.common.IdempotencyId
import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.product.StockQuantity
import jp.inaba.message.CommandResult
import jp.inaba.message.InabaEventTag
import jp.inaba.message.product.command.DecreaseStockCommand
import jp.inaba.message.product.command.DecreaseStockResult
import jp.inaba.message.product.event.ProductCreatedEvent
import jp.inaba.message.product.event.ProductDeletedEvent
import jp.inaba.message.product.event.StockDecreasedEvent
import jp.inaba.message.product.event.StockIncreasedEvent
import org.axonframework.eventsourcing.annotation.EventSourcingHandler
import org.axonframework.eventsourcing.annotation.reflection.EntityCreator
import org.axonframework.extension.spring.stereotype.EventSourced
import org.axonframework.messaging.commandhandling.annotation.CommandHandler
import org.axonframework.messaging.eventhandling.gateway.EventAppender
import org.axonframework.modelling.annotation.InjectEntity
import org.springframework.stereotype.Component

@Component
class DecreaseStockCommandHandler {
    @CommandHandler
    fun handle(
        command: DecreaseStockCommand,
        @InjectEntity state: State,
        eventAppender: EventAppender,
    ): CommandResult {
        if (!state.created) {
            return DecreaseStockResult.notFound()
        }
        if (state.deleted) {
            return DecreaseStockResult.deleted()
        }
        // 冪等性を考慮し、すでに処理済みの場合は成功を返す
        if (state.idempotenceChecker.isIdempotent(command.idempotencyId)) {
            return DecreaseStockResult.success()
        }

        val currentQuantity = StockQuantity(state.quantity)
        if (currentQuantity.canNotSubtract(command.decreaseStockQuantity)) {
            return DecreaseStockResult.insufficientStock()
        }

        val decreasedQuantity = currentQuantity.subtract(command.decreaseStockQuantity)

        eventAppender.append(
            StockDecreasedEvent(
                id = command.id.value,
                idempotencyId = command.idempotencyId.value,
                decreaseStockQuantity = command.decreaseStockQuantity.value,
                decreasedStockQuantity = decreasedQuantity.value,
            ),
        )

        return DecreaseStockResult.success()
    }

    @EventSourced(tagKey = InabaEventTag.PRODUCT_ID, idType = ProductId::class)
    class State(
        var created: Boolean,
        var deleted: Boolean,
        var quantity: Int,
        var idempotenceChecker: IdempotenceChecker,
    ) {
        @EntityCreator
        constructor() : this(
            created = false,
            deleted = false,
            quantity = 0,
            idempotenceChecker = IdempotenceChecker(),
        )

        @EventSourcingHandler
        fun evolve(event: ProductCreatedEvent) {
            created = true
            quantity = event.quantity
        }

        @EventSourcingHandler
        fun evolve(event: ProductDeletedEvent) {
            deleted = true
        }

        @EventSourcingHandler
        fun evolve(event: StockDecreasedEvent) {
            quantity = event.decreasedStockQuantity
            idempotenceChecker.register(IdempotencyId(event.idempotencyId))
        }

        @EventSourcingHandler
        fun evolve(event: StockIncreasedEvent) {
            quantity = event.increasedStockQuantity
            idempotenceChecker.register(IdempotencyId(event.idempotencyId))
        }
    }
}
