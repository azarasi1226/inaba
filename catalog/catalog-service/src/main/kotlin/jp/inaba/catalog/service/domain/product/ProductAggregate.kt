package jp.inaba.catalog.service.domain.product

import jp.inaba.catalog.api.domain.product.*
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
    private lateinit var imageUrl: ProductImageURL
    private lateinit var price: ProductPrice
    private lateinit var quantity: ProductQuantity

    @CommandHandler
    constructor(command: ProductCommands.Create): this() {
        val event = ProductEvents.Created(
            id = command.id,
            name = command.name,
            description = command.description,
            imageUrl = command.imageUrl,
            price = command.price,
            quantity = command.quantity
        )

        AggregateLifecycle.apply(event)
    }

    @EventSourcingHandler
    fun on(event: ProductEvents.Created) {
        id = event.id
        name = event.name
        description = event.description
        imageUrl = event.imageUrl
        price = event.price
        quantity = event.quantity
    }
    @CommandHandler
    constructor(command: ProductCommands.Update): this() {
        val event = ProductEvents.Updated(
                id = command.id,
                name = command.name,
                description = command.description,
                imageUrl = command.imageUrl,
                price = command.price,
                quantity = command.quantity
        )

        AggregateLifecycle.apply(event)
    }

    @EventSourcingHandler
    fun on(event: ProductEvents.Updated) {
        id = event.id
        name = event.name
        description = event.description
        imageUrl = event.imageUrl
        price = event.price
        quantity = event.quantity
    }
    @CommandHandler
    constructor(command: ProductCommands.Delete): this() {
        val event = ProductEvents.Deleted(
            id = command.id
        )
        AggregateLifecycle.apply(event)
    }

    @EventSourcingHandler
    fun on(event: ProductEvents.Deleted) {
        id = event.id
    }
}