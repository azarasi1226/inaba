package jp.inaba.basket.service.domain.basket

import jp.inaba.basket.api.domain.basket.*
import jp.inaba.catalog.api.domain.product.ProductId
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateCreationPolicy
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.modelling.command.CreationPolicy
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class BasketAggregate() {
    @AggregateIdentifier
    private lateinit var id: BasketId
    private var items = mutableMapOf<ProductId, BasketItemQuantity>()

    companion object {
        private const val MAX_ITEM_CAPACITY = 50
    }

    @CommandHandler
    @CreationPolicy(AggregateCreationPolicy.CREATE_IF_MISSING)
    fun handle(command: BasketCommands.SetBasketItem) {
        // 買い物かごの中の最大種類に達しているか？
        if(items.size >= MAX_ITEM_CAPACITY) {
            throw Exception("カートの中に入れられる商品種類の制限に引っかかったよ")
        }

        val event = BasketEvents.BasketItemSet(
            id = command.id.value,
            productId = command.productId.value,
            basketItemQuantity = command.basketItemQuantity.value
        )

        AggregateLifecycle.apply(event)
    }

    @CommandHandler
    fun handle(command: BasketCommands.DeleteBasketItem) {
        if(!items.keys.contains(command.productId)) {
            // productIdが存在しなかったら例外でもいい気がするけど、
            // 冪等性な操作を実現したほうがいいような気もするから早期return
            return
        }

        val event = BasketEvents.BasketItemDeleted(
            id = command.id.value,
            productId = command.productId.value
        )

        AggregateLifecycle.apply(event)
    }

    @CommandHandler
    fun handle(command: BasketCommands.Clear) {
        val event = BasketEvents.Cleared(command.id.value)

        AggregateLifecycle.apply(event)
    }

    @EventSourcingHandler
    fun on(event: BasketEvents.BasketItemSet) {
        val productId = ProductId(event.productId)
        val quantity = BasketItemQuantity(event.basketItemQuantity)

        id = BasketId(event.id)
        items[productId] = quantity
    }

    @EventSourcingHandler
    fun on(event: BasketEvents.BasketItemDeleted) {
        val productId = ProductId(event.productId)

        items.remove(productId)
    }
}