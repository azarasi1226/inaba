package jp.inaba.service.domain.stock

import jp.inaba.core.domain.common.ActionCommandResult
import jp.inaba.core.domain.common.IdempotenceChecker
import jp.inaba.core.domain.stock.DecreaseStockError
import jp.inaba.core.domain.stock.IncreaseStockError
import jp.inaba.core.domain.stock.StockId
import jp.inaba.core.domain.stock.StockQuantity
import jp.inaba.message.stock.command.CreateStockCommand
import jp.inaba.message.stock.command.DecreaseStockCommand
import jp.inaba.message.stock.command.IncreaseStockCommand
import jp.inaba.message.stock.event.StockCreatedEvent
import jp.inaba.message.stock.event.StockDecreasedEvent
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
    fun handle(command: IncreaseStockCommand): ActionCommandResult {
        // 冪等性チェック
        if (idempotenceChecker.isIdempotent(command.idempotencyId)) {
            return ActionCommandResult.ok()
        }
        // 在庫増やせる？
        if (!quantity.canAdd(command.increaseCount)) {
            return ActionCommandResult.error(IncreaseStockError.OutOfStock.errorCode)
        }

        val event =
            StockIncreasedEvent(
                id = command.id.value,
                idempotencyId = command.idempotencyId.value,
                increaseCount = command.increaseCount.value,
            )

        AggregateLifecycle.apply(event)

        return ActionCommandResult.ok()
    }

    @CommandHandler
    fun handle(command: DecreaseStockCommand): ActionCommandResult {
        // 冪等性チェック
        if (idempotenceChecker.isIdempotent(command.idempotencyId)) {
            return ActionCommandResult.ok()
        }
        // 在庫減らせる？
        if (!quantity.canSubtract(command.decreaseCount)) {
            return ActionCommandResult.error(DecreaseStockError.InsufficientStock.errorCode)
        }

        val event =
            StockDecreasedEvent(
                id = command.id.value,
                idempotencyId = command.idempotencyId.value,
                decreaseCount = command.decreaseCount.value,
            )

        AggregateLifecycle.apply(event)

        return ActionCommandResult.ok()
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
}
