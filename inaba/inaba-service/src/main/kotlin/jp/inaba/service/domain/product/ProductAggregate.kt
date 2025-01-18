package jp.inaba.service.domain.product

import jp.inaba.core.domain.common.ActionCommandResult
import jp.inaba.core.domain.common.IdempotenceChecker
import jp.inaba.core.domain.common.IdempotencyId
import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.product.ProductQuantity
import jp.inaba.core.domain.product.ShipmentProductError
import jp.inaba.message.product.command.CreateProductCommand
import jp.inaba.message.product.command.DeleteProductCommand
import jp.inaba.message.product.command.InboundProductCommand
import jp.inaba.message.product.command.ShipmentProductCommand
import jp.inaba.message.product.command.UpdateProductCommand
import jp.inaba.message.product.event.ProductCreatedEvent
import jp.inaba.message.product.event.ProductDeletedEvent
import jp.inaba.message.product.event.ProductInboundedEvent
import jp.inaba.message.product.event.ProductShippedEvent
import jp.inaba.message.product.event.ProductUpdatedEvent
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class ProductAggregate() {
    @AggregateIdentifier
    private lateinit var id: ProductId
    private lateinit var quantity: ProductQuantity

    private var idempotenceChecker = IdempotenceChecker()

    @CommandHandler
    constructor(command: CreateProductCommand) : this() {
        val event =
            ProductCreatedEvent(
                id = command.id.value,
                name = command.name.value,
                description = command.description.value,
                imageUrl = command.imageUrl?.value,
                price = command.price.value,
                quantity = command.quantity.value,
            )

        AggregateLifecycle.apply(event)
    }

    @CommandHandler
    fun handle(command: UpdateProductCommand) {
        val event =
            ProductUpdatedEvent(
                id = command.id.value,
                name = command.name.value,
                description = command.description.value,
                imageUrl = command.imageUrl?.value,
                price = command.price.value,
            )

        AggregateLifecycle.apply(event)
    }

    @CommandHandler
    fun handle(command: InboundProductCommand) {
        // 冪等性チェック
        if (idempotenceChecker.isIdempotent(command.idempotencyId)) {
            return
        }

        val event =
            ProductInboundedEvent(
                id = command.id.value,
                idempotencyId = command.idempotencyId.value,
                quantity = command.quantity.value,
            )

        AggregateLifecycle.apply(event)
    }

    @CommandHandler
    fun handle(command: ShipmentProductCommand) : ActionCommandResult {
        // 冪等性チェック
        if (idempotenceChecker.isIdempotent(command.idempotencyId)) {
            return ActionCommandResult.ok()
        }

        // 出荷数量分の在庫はある？
        if (quantity.value >= command.quantity.value) {
            return ActionCommandResult.error(ShipmentProductError.OutOfStock.errorCode)
        }

        val event =
            ProductShippedEvent(
                id = command.id.value,
                idempotencyId = command.idempotencyId.value,
                quantity = command.quantity.value,
            )

        AggregateLifecycle.apply(event)

        return ActionCommandResult.ok()
    }

    @CommandHandler
    fun handle(command: DeleteProductCommand) {
        val event = ProductDeletedEvent(command.id.value)

        AggregateLifecycle.apply(event)
    }

    @EventSourcingHandler
    fun on(event: ProductCreatedEvent) {
        id = ProductId(event.id)
        quantity = ProductQuantity(event.quantity)
    }

    @EventSourcingHandler
    fun on(event: ProductUpdatedEvent) {
    }

    @EventSourcingHandler
    fun on(event: ProductInboundedEvent) {
        quantity = ProductQuantity(event.quantity)
        idempotenceChecker.register(IdempotencyId(event.idempotencyId))
    }

    @EventSourcingHandler
    fun on(event: ProductShippedEvent) {
        quantity = ProductQuantity(event.quantity)
        idempotenceChecker.register(IdempotencyId(event.idempotencyId))
    }

    @EventSourcingHandler
    fun on(event: ProductDeletedEvent) {
        AggregateLifecycle.markDeleted()
    }
}
