package jp.inaba.catalog.service.domain.product

import jp.inaba.catalog.api.domain.product.ProductCommands
import jp.inaba.catalog.api.domain.product.ProductDescription
import jp.inaba.catalog.api.domain.product.ProductEvents
import jp.inaba.catalog.api.domain.product.ProductId
import jp.inaba.catalog.api.domain.product.ProductImageURL
import jp.inaba.catalog.api.domain.product.ProductName
import jp.inaba.catalog.api.domain.product.ProductPrice
import jp.inaba.catalog.api.domain.product.ProductQuantity
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
    constructor(command: ProductCommands.Create) : this() {
        val event =
            ProductEvents.Created(
                id = command.id.value,
                name = command.name.value,
                description = command.description.value,
                imageUrl = command.imageUrl.value,
                price = command.price.value,
                quantity = command.quantity.value,
            )

        AggregateLifecycle.apply(event)
    }

    @EventSourcingHandler
    fun on(event: ProductEvents.Created) {
        id = ProductId(event.id)
        name = ProductName(event.name)
        description = ProductDescription(event.description)
        imageUrl = ProductImageURL(event.imageUrl)
        price = ProductPrice(event.price)
        quantity = ProductQuantity(event.quantity)
    }

    @CommandHandler
    constructor(command: ProductCommands.Update) : this() {
        val event =
            ProductEvents.Updated(
                id = command.id.value,
                name = command.name.value,
                description = command.description.value,
                imageUrl = command.imageUrl.value,
                price = command.price.value,
                quantity = command.quantity.value,
            )

        AggregateLifecycle.apply(event)
    }

    @EventSourcingHandler
    fun on(event: ProductEvents.Updated) {
        id = ProductId(event.id)
        name = ProductName(event.name)
        description = ProductDescription(event.description)
        imageUrl = ProductImageURL(event.imageUrl)
        price = ProductPrice(event.price)
        quantity = ProductQuantity(event.quantity)
    }

    @CommandHandler
    constructor(command: ProductCommands.Delete) : this() {
        val event =
            ProductEvents.Deleted(
                id = command.id.value,
            )
        AggregateLifecycle.apply(event)
    }

    @EventSourcingHandler
    fun on(event: ProductEvents.Deleted) {
        // Todo 削除実装
    }
}
