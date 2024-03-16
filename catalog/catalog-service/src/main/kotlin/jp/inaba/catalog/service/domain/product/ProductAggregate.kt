package jp.inaba.catalog.service.domain.product

import jp.inaba.catalog.api.domain.product.CreateProductCommand
import jp.inaba.catalog.api.domain.product.ProductCreatedEvent
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
    constructor(command: CreateProductCommand): this() {
        val event = ProductCreatedEvent(
            id = command.id,
            productName = command.productName,
            description = command.description,
            imageUrl = command.imageUrl,
            price = command.price,
            quantity = command.quantity
        )

        AggregateLifecycle.apply(event)
    }

    @EventSourcingHandler
    fun on(event: ProductCreatedEvent) {
        id = event.id
    }
}