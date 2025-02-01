package jp.inaba.service.domain.product

import jp.inaba.core.domain.common.UseCaseException
import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.product.ShipmentProductError
import jp.inaba.message.product.command.CreateProductCommand
import jp.inaba.message.product.command.DeleteProductCommand
import jp.inaba.message.product.command.UpdateProductCommand
import jp.inaba.message.product.event.ProductCreatedEvent
import jp.inaba.message.product.event.ProductDeletedEvent
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

    @CommandHandler
    constructor(command: CreateProductCommand) : this() {
        val event =
            ProductCreatedEvent(
                id = command.id.value,
                name = command.name.value,
                description = command.description.value,
                imageUrl = command.imageUrl?.value,
                price = command.price.value,
            )

        //throw UseCaseException(ShipmentProductError.OutOfStock)

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
    fun handle(command: DeleteProductCommand) {
        val event = ProductDeletedEvent(command.id.value)

        AggregateLifecycle.apply(event)
    }

    @EventSourcingHandler
    fun on(event: ProductCreatedEvent) {
        id = ProductId(event.id)
    }

    @EventSourcingHandler
    fun on(event: ProductUpdatedEvent) {
        // 何もすることがねぇ....
    }

    @EventSourcingHandler
    fun on(event: ProductDeletedEvent) {
        AggregateLifecycle.markDeleted()
    }
}
