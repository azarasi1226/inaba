package jp.inaba.basket.service.domain.basket

import jp.inaba.basket.api.domain.basket.BasketClearedEvent
import jp.inaba.basket.api.domain.basket.BasketCreatedEvent
import jp.inaba.basket.api.domain.basket.BasketDeletedEvent
import jp.inaba.basket.api.domain.basket.BasketId
import jp.inaba.basket.api.domain.basket.BasketItemDeletedEvent
import jp.inaba.basket.api.domain.basket.BasketItemQuantity
import jp.inaba.basket.api.domain.basket.BasketItemSetEvent
import jp.inaba.basket.api.domain.basket.ClearBasketCommand
import jp.inaba.basket.api.domain.basket.ClearBasketError
import jp.inaba.basket.api.domain.basket.DeleteBasketCommand
import jp.inaba.basket.api.domain.basket.DeleteBasketError
import jp.inaba.basket.api.domain.basket.DeleteBasketItemCommand
import jp.inaba.basket.api.domain.basket.DeleteBasketItemError
import jp.inaba.basket.api.domain.basket.SetBasketItemError
import jp.inaba.catalog.api.domain.product.ProductId
import jp.inaba.common.domain.shared.ActionCommandResult
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
    fun handle(command: InternalSetBasketItemCommand): ActionCommandResult {
        // 買い物かごの中のアイテムが最大種類に達している？
        if (items.size >= MAX_ITEM_KIND_COUNT) {
            return ActionCommandResult.error(SetBasketItemError.PRODUCT_MAX_KIND_OVER.errorCode)
        }

        val event =
            BasketItemSetEvent(
                id = command.id.value,
                productId = command.productId.value,
                basketItemQuantity = command.basketItemQuantity.value,
            )

        AggregateLifecycle.apply(event)

        return ActionCommandResult.ok()
    }

    @CommandHandler
    fun handle(command: DeleteBasketItemCommand): ActionCommandResult {
        val event =
            BasketItemDeletedEvent(
                id = command.id.value,
                productId = command.productId.value,
            )

        AggregateLifecycle.apply(event)

        return ActionCommandResult.ok()
    }

    @CommandHandler
    fun handle(command: ClearBasketCommand): ActionCommandResult {
        val event = BasketClearedEvent(command.id.value)

        AggregateLifecycle.apply(event)

        return ActionCommandResult.ok()
    }

    @CommandHandler
    fun handle(command: DeleteBasketCommand): ActionCommandResult {
        val event = BasketClearedEvent(command.id.value)

        AggregateLifecycle.apply(event)

        return ActionCommandResult.ok()
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
        val productId = ProductId(event.productId)

        items.remove(productId)
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
