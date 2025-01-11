package jp.inaba.service.domain.product

import jp.inaba.core.domain.product.ProductDescription
import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.product.ProductImageURL
import jp.inaba.core.domain.product.ProductName
import jp.inaba.core.domain.product.ProductPrice
import jp.inaba.core.domain.product.ProductQuantity
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
    private lateinit var name: ProductName
    private lateinit var description: ProductDescription
    private var imageUrl: ProductImageURL? = null
    private lateinit var price: ProductPrice
    private lateinit var quantity: ProductQuantity

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
    fun handle(command: ShipmentProductCommand) {
        val event =
            ProductShippedEvent(
                id = command.id.value,
                quantity = command.quantity.value,
            )

        AggregateLifecycle.apply(event)
    }

    @CommandHandler
    fun handle(command: InboundProductCommand) {
        val event =
            ProductInboundedEvent(
                id = command.id.value,
                quantity = command.quantity.value,
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
        name = ProductName(event.name)
        description = ProductDescription(event.description)
        imageUrl = ProductImageURL(event.imageUrl)
        price = ProductPrice(event.price)
        quantity = ProductQuantity(event.quantity)
    }

    @EventSourcingHandler
    fun on(event: ProductUpdatedEvent) {
        name = ProductName(event.name)
        description = ProductDescription(event.description)
        imageUrl = ProductImageURL(event.imageUrl)
        price = ProductPrice(event.price)
    }

    @EventSourcingHandler
    fun on(event: ProductShippedEvent) {
        quantity = ProductQuantity(event.quantity)
    }

    @EventSourcingHandler
    fun on(event: ProductInboundedEvent) {
        quantity = ProductQuantity(event.quantity)
    }

    @EventSourcingHandler
    fun on(event: ProductDeletedEvent) {
        AggregateLifecycle.markDeleted()
    }
}
