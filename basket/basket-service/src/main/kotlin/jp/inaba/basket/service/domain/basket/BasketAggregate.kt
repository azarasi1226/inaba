package jp.inaba.basket.service.domain.basket

import jp.inaba.basket.api.domain.basket.BasketCommands
import jp.inaba.basket.api.domain.basket.BasketEvents
import jp.inaba.basket.api.domain.basket.BasketId
import jp.inaba.basket.api.domain.basket.BasketItemQuantity
import jp.inaba.catalog.api.domain.product.ProductId
import jp.inaba.common.domain.shared.DomainException
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
        private const val MAX_ITEM_KIND_COUNT = 50
    }

    @CommandHandler
    @CreationPolicy(AggregateCreationPolicy.CREATE_IF_MISSING)
    fun handle(command: BasketCommands.SetBasketItem) {
        // 買い物かごの中のアイテムが最大種類に達しているか？
        if(items.size >= MAX_ITEM_KIND_COUNT) {
            throw DomainException("カートの中に入れられる商品種類の制限に引っかかったよ")
        }

        // 追加対象が買い物かごに既に存在し、かつ数量も同じだったらイベントを出さずに早期return
        val quantity = items[command.productId]
        if (quantity != null &&
            command.basketItemQuantity == quantity){
            return
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
        // 削除対象が存在しなかったらイベントを出さずに早期return
        if(!items.keys.contains(command.productId)) {
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