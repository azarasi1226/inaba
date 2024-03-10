package jp.inaba.basket.service.domain.basket

import jp.ianba.basket.api.domain.basket.*
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class BasketAggregate() {
    @AggregateIdentifier
    private lateinit var id: BasketId
    //TODO(ItemIdの値オブジェクトにする)
    private var items = mutableMapOf<String, ItemQuantity>()

    companion object {
        private const val MAX_ITEM_CAPACITY = 50
    }

    @CommandHandler
    constructor(command: CreateBasketCommand): this() {
        val event = BasketCreatedEvent(
            id = command.id,
            userId = command.userId
        )

        AggregateLifecycle.apply(event)
    }

    @CommandHandler
    fun handle(command: SetItemCommand) {
        // 種類の個数をチェック
        if(items.size >= MAX_ITEM_CAPACITY) {
            throw Exception("カートの中に入れられる商品種類の制限に引っかかったよ")
        }

        val event = ItemSetEvent(
            id = command.id,
            itemId = command.itemId,
            itemQuantity = command.itemQuantity
        )

        AggregateLifecycle.apply(event)
    }

    @CommandHandler
    fun handle(command: DeleteItemCommand) {
        val event = ItemDeletedEvent(
            id = command.id,
            itemId = command.itemId
        )

        AggregateLifecycle.apply(event)
    }

    @CommandHandler
    fun handle(command: ClearBasketCommand) {
        val event = ClearBasketCommand(command.id)

        AggregateLifecycle.apply(event)
    }

    @EventSourcingHandler
    fun on(event: BasketCreatedEvent) {
        id = event.id
    }

    @EventSourcingHandler
    fun on(event: ItemSetEvent) {
        items[event.itemId] = event.itemQuantity
    }

    @EventSourcingHandler
    fun on(event: ItemDeletedEvent) {
        items.remove(event.itemId)
    }
}