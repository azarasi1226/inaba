package jp.inaba.catalog.service.domain.product

import jp.inaba.catalog.api.domain.product.CreateProductCommand
import jp.inaba.catalog.api.domain.product.DeleteProductCommand
import jp.inaba.catalog.api.domain.product.InboundProductCommand
import jp.inaba.catalog.api.domain.product.ProductCreatedEvent
import jp.inaba.catalog.api.domain.product.ProductDeletedEvent
import jp.inaba.catalog.api.domain.product.ProductDescription
import jp.inaba.catalog.api.domain.product.ProductId
import jp.inaba.catalog.api.domain.product.ProductImageURL
import jp.inaba.catalog.api.domain.product.ProductInboundEvent
import jp.inaba.catalog.api.domain.product.ProductName
import jp.inaba.catalog.api.domain.product.ProductPrice
import jp.inaba.catalog.api.domain.product.ProductQuantity
import jp.inaba.catalog.api.domain.product.ProductShippedEvent
import jp.inaba.catalog.api.domain.product.ProductUpdatedEvent
import jp.inaba.catalog.api.domain.product.ShipmentProductCommand
import jp.inaba.catalog.api.domain.product.UpdateProductCommand
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
    private var isDeleted = false

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
                quantity = command.quantity.value
            )

        AggregateLifecycle.apply(event)
    }

    @CommandHandler
    fun handle(command: InboundProductCommand) {
        val event =
            ProductInboundEvent(
                id = command.id.value,
                quantity = command.quantity.value
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
    fun on(event: ProductInboundEvent) {
        quantity = ProductQuantity(event.quantity)
    }

    @EventSourcingHandler
    fun on(event: ProductDeletedEvent) {
        isDeleted = true
    }
}
