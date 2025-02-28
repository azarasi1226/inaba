package jp.inaba.service.domain.basket

import jp.inaba.core.domain.basket.BasketId
import jp.inaba.core.domain.basket.BasketItemQuantity
import jp.inaba.core.domain.basket.SetBasketItemError
import jp.inaba.core.domain.common.UseCaseException
import jp.inaba.core.domain.product.ProductId
import jp.inaba.message.basket.command.ClearBasketCommand
import jp.inaba.message.basket.command.DeleteBasketCommand
import jp.inaba.message.basket.command.DeleteBasketItemCommand
import jp.inaba.message.basket.event.BasketClearedEvent
import jp.inaba.message.basket.event.BasketCreatedEvent
import jp.inaba.message.basket.event.BasketDeletedEvent
import jp.inaba.message.basket.event.BasketItemDeletedEvent
import jp.inaba.message.basket.event.BasketItemSetEvent
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class BasketAggregate() {
    @AggregateIdentifier
    private lateinit var id: BasketId
    private var items = mutableMapOf<ProductId, BasketItemQuantity>()

    companion object {
        private const val MAX_ITEM_KIND_COUNT = 50
    }

    @CommandHandler
    constructor(command: InternalCreateBasketCommand) : this() {
        val event =
            BasketCreatedEvent(
                id = command.id.value,
                userId = command.userId.value,
            )

        AggregateLifecycle.apply(event)
    }

    @CommandHandler
    fun handle(command: InternalSetBasketItemCommand) {
        // 買い物かごの中のアイテムが最大種類に達している？
        if (items.size >= MAX_ITEM_KIND_COUNT) {
            throw UseCaseException(SetBasketItemError.PRODUCT_MAX_KIND_OVER)
        }

        val event =
            BasketItemSetEvent(
                id = command.id.value,
                productId = command.productId.value,
                basketItemQuantity = command.basketItemQuantity.value,
            )

        AggregateLifecycle.apply(event)
    }

    @CommandHandler
    fun handle(command: DeleteBasketItemCommand) {
        val event =
            BasketItemDeletedEvent(
                id = command.id.value,
                productId = command.productId.value,
            )

        AggregateLifecycle.apply(event)
    }

    @CommandHandler
    fun handle(command: ClearBasketCommand) {
        val event = BasketClearedEvent(command.id.value)
        AggregateLifecycle.apply(event)
    }

    @CommandHandler
    fun handle(command: DeleteBasketCommand) {
        val event = BasketClearedEvent(command.id.value)
        AggregateLifecycle.apply(event)
    }

    @EventSourcingHandler
    fun on(event: BasketCreatedEvent) {
        id = BasketId(event.id)
    }

    @EventSourcingHandler
    fun on(event: BasketItemSetEvent) {
        val productId = ProductId(event.productId)
        val quantity = BasketItemQuantity(event.basketItemQuantity)

        items[productId] = quantity
    }

    @EventSourcingHandler
    fun on(event: BasketItemDeletedEvent) {
        items.remove(ProductId(event.productId))
    }

    @EventSourcingHandler
    fun on(event: BasketClearedEvent) {
        items.clear()
    }

    @EventSourcingHandler
    fun on(event: BasketDeletedEvent) {
        AggregateLifecycle.markDeleted()
    }
}
