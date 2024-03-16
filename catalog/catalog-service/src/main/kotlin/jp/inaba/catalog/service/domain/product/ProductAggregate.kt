package jp.inaba.catalog.service.domain.product

import jp.inaba.catalog.api.domain.product.ProductCommands
import jp.inaba.catalog.api.domain.product.ProductEvents
import jp.inaba.catalog.api.domain.product.ProductId
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
    }
}