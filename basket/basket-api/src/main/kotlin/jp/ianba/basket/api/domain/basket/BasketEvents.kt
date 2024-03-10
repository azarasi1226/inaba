package jp.ianba.basket.api.domain.basket

sealed interface BasketEvent {
    val id: BasketId
}

data class BasketCreatedEvent(
    override val id: BasketId,
    val userId: String
) : BasketEvent

data class ItemSetEvent(
    override val id: BasketId,
    val itemId: String,
    val itemQuantity: ItemQuantity
) : BasketEvent

data class ItemDeletedEvent(
    override val id: BasketId,
    val itemId: String
) : BasketEvent

data class BasketClearedEvent(
    override val id: BasketId
) : BasketEvent