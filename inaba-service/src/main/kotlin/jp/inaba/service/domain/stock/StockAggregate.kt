package jp.inaba.service.domain.stock

import jp.inaba.core.domain.common.IdempotenceChecker
import jp.inaba.core.domain.common.IdempotencyId
import jp.inaba.core.domain.common.UseCaseException
import jp.inaba.core.domain.product.DecreaseStockError
import jp.inaba.core.domain.product.IncreaseStockError
import jp.inaba.core.domain.stock.StockId
import jp.inaba.core.domain.product.StockQuantity
import jp.inaba.message.product.command.DecreaseStockCommand
import jp.inaba.message.stock.command.DeleteStockCommand
import jp.inaba.message.product.command.IncreaseStockCommand
import jp.inaba.message.stock.event.StockCreatedEvent
import jp.inaba.message.product.event.StockDecreasedEvent
import jp.inaba.message.stock.event.StockDeletedEvent
import jp.inaba.message.product.event.StockIncreasedEvent
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class StockAggregate() {
    @AggregateIdentifier
    private lateinit var id: StockId
    private lateinit var quantity: StockQuantity

    private var idempotenceChecker = IdempotenceChecker()

    @CommandHandler
    constructor(command: InternalCreateStockCommand) : this() {
        val event =
            StockCreatedEvent(
                id = command.id.value,
                productId = command.productId.value,
            )

        AggregateLifecycle.apply(event)
    }

    @CommandHandler
    fun handle(command: IncreaseStockCommand) {
        // 冪等性チェック
        if (idempotenceChecker.isIdempotent(command.idempotencyId)) {
            return
        }
        // 在庫増やせる？
        if (quantity.canNotAdd(command.increaseStockQuantity)) {
            throw UseCaseException(IncreaseStockError.CANNOT_RECEIVE)
        }

        val increasedStockQuantity = quantity.add(command.increaseStockQuantity)

        val event =
            StockIncreasedEvent(
                id = command.id.value,
                increaseStockQuantity = command.increaseStockQuantity.value,
                idempotencyId = command.idempotencyId.value,
                increasedStockQuantity = increasedStockQuantity.value,
            )

        AggregateLifecycle.apply(event)
    }

    @CommandHandler
    fun handle(command: DecreaseStockCommand) {
        // 冪等性チェック
        if (idempotenceChecker.isIdempotent(command.idempotencyId)) {
            return
        }
        // 在庫減らせる？
        if (quantity.canNotSubtract(command.decreaseStockQuantity)) {
            throw UseCaseException(DecreaseStockError.INSUFFICIENT_STOCK)
        }

        val decreasedStockQuantity = quantity.subtract(command.decreaseStockQuantity)

        val event =
            StockDecreasedEvent(
                id = command.id.value,
                decreaseStockQuantity = command.decreaseStockQuantity.value,
                idempotencyId = command.idempotencyId.value,
                decreasedStockQuantity = decreasedStockQuantity.value,
            )

        AggregateLifecycle.apply(event)
    }

    @CommandHandler
    fun handle(command: DeleteStockCommand) {
        val event =
            StockDeletedEvent(
                id = command.id.value,
            )

        AggregateLifecycle.apply(event)
    }

    @EventSourcingHandler
    fun on(event: StockCreatedEvent) {
        id = StockId(event.id)
        quantity = StockQuantity(0)
    }

    @EventSourcingHandler
    fun on(event: StockIncreasedEvent) {
        quantity = StockQuantity(event.increasedStockQuantity)
        idempotenceChecker.register(IdempotencyId(event.idempotencyId))
    }

    @EventSourcingHandler
    fun on(event: StockDecreasedEvent) {
        quantity = StockQuantity(event.decreasedStockQuantity)
        idempotenceChecker.register(IdempotencyId(event.idempotencyId))
    }

    @EventSourcingHandler
    fun on(event: StockDeletedEvent) {
        AggregateLifecycle.markDeleted()
    }
}
