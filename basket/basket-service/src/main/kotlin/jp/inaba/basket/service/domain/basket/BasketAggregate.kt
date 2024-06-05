package jp.inaba.basket.service.domain.basket

import jp.inaba.basket.api.domain.basket.BasketCommands
import jp.inaba.basket.api.domain.basket.BasketErrors
import jp.inaba.basket.api.domain.basket.BasketEvents
import jp.inaba.basket.api.domain.basket.BasketId
import jp.inaba.basket.api.domain.basket.BasketItemQuantity
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
    //TODO("ここそのままUserIdでいいのでは？")
    private lateinit var id: BasketId
    private var items = mutableMapOf<ProductId, BasketItemQuantity>()

    companion object {
        private const val MAX_ITEM_KIND_COUNT = 50
    }

    @CommandHandler
    constructor(command: InternalBasketCommands.Create) : this() {
        val event = BasketEvents.Created(command.id.value)

        AggregateLifecycle.apply(event)
    }

    @CommandHandler
    fun handle(command: InternalBasketCommands.SetBasketItem): ActionCommandResult {
        // 買い物かごの中のアイテムが最大種類に達しているか？
        if (items.size >= MAX_ITEM_KIND_COUNT) {
            return ActionCommandResult.error(BasketErrors.SetBasketItem.PRODUCT_MAX_KIND_OVER.errorCode)
        }

        val event =
            BasketEvents.BasketItemSet(
                id = command.id.value,
                productId = command.productId.value,
                basketItemQuantity = command.basketItemQuantity.value,
            )

        AggregateLifecycle.apply(event)

        return ActionCommandResult.ok()
    }

    @CommandHandler
    fun handle(command: BasketCommands.DeleteBasketItem) {
        val event =
            BasketEvents.BasketItemDeleted(
                id = command.id.value,
                productId = command.productId.value,
            )

        AggregateLifecycle.apply(event)
    }

    @CommandHandler
    fun handle(command: BasketCommands.Clear) {
        val event = BasketEvents.Cleared(command.id.value)

        AggregateLifecycle.apply(event)
    }

    @EventSourcingHandler
    fun on(event: BasketEvents.Created) {
        id = BasketId(event.id)
    }

    @EventSourcingHandler
    fun on(event: BasketEvents.BasketItemSet) {
        val productId = ProductId(event.productId)
        val quantity = BasketItemQuantity(event.basketItemQuantity)

        items[productId] = quantity
    }

    @EventSourcingHandler
    fun on(event: BasketEvents.BasketItemDeleted) {
        val productId = ProductId(event.productId)

        items.remove(productId)
    }

    @EventSourcingHandler
    fun on(event: BasketEvents.Cleared) {
        items.clear()
    }
}
