package jp.inaba.service.domain.stock

import jp.inaba.core.domain.common.IdempotenceChecker
import jp.inaba.core.domain.common.UseCaseException
import jp.inaba.core.domain.stock.DecreaseStockError
import jp.inaba.core.domain.stock.IncreaseStockError
import jp.inaba.core.domain.stock.StockId
import jp.inaba.core.domain.stock.StockQuantity
import jp.inaba.message.stock.command.DecreaseStockCommand
import jp.inaba.message.stock.command.DeleteStockCommand
import jp.inaba.message.stock.command.IncreaseStockCommand
import jp.inaba.message.stock.event.StockCreatedEvent
import jp.inaba.message.stock.event.StockDecreasedEvent
import jp.inaba.message.stock.event.StockDeletedEvent
import jp.inaba.message.stock.event.StockIncreasedEvent
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
        if (!quantity.canAdd(command.increaseCount)) {
            throw UseCaseException(IncreaseStockError.OutOfStock)
        }

        val event =
            StockIncreasedEvent(
                id = command.id.value,
                idempotencyId = command.idempotencyId.value,
                increaseCount = command.increaseCount.value,
                stockQuantity = quantity.value
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
        if (!quantity.canSubtract(command.decreaseCount)) {
            throw UseCaseException(DecreaseStockError.InsufficientStock)
        }

        val event =
            StockDecreasedEvent(
                id = command.id.value,
                idempotencyId = command.idempotencyId.value,
                decreaseCount = command.decreaseCount.value,
                stockQuantity = quantity.value
            )

        AggregateLifecycle.apply(event)
    }

    @CommandHandler
    fun handle(command: DeleteStockCommand) {
        val event =
            StockDeletedEvent(
                id = command.id.value
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
        val increaseCount = StockQuantity(event.increaseCount)
        quantity = quantity.add(increaseCount)
    }

    @EventSourcingHandler
    fun on(event: StockDecreasedEvent) {
        val decreaseCount = StockQuantity(event.decreaseCount)
        quantity = quantity.subtract(decreaseCount)
    }

    @EventSourcingHandler
    fun on(event: StockDeletedEvent) {
        AggregateLifecycle.markDeleted()
    }
}
