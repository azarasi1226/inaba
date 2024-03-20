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
    private lateinit var description: String
    private lateinit var imageUrl: String
    private lateinit var price: ProductPrice
    private var quantity: Int = 0

    @CommandHandler
    constructor(command: ProductCommands.Create): this() {
        val event = ProductEvents.Created(
            id = command.id.value,
            name = command.name.value,
            description = command.description,
            imageUrl = command.imageUrl,
            price = command.price.value,
            quantity = command.quantity
        )

        AggregateLifecycle.apply(event)
    }

    @EventSourcingHandler
    fun on(event: ProductEvents.Created) {
        id = ProductId(event.id)
        name = ProductName(event.name)
        description = event.description
        imageUrl = event.imageUrl
        price = ProductPrice(event.price)
        quantity = event.quantity
    }
}