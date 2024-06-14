package jp.inaba.basket.api.domain.basket

sealed interface BasketEvent {
    val id: String
}

data class BasketCreatedEvent(
    override val id: String,
    val userId: String,
) : BasketEvent

data class BasketItemSetEvent(
    override val id: String,
    val productId: String,
    val basketItemQuantity: Int,
) : BasketEvent

data class BasketItemDeletedEvent(
    override val id: String,
    val productId: String,
) : BasketEvent

data class BasketClearedEvent(
    override val id: String,
) : BasketEvent

data class BasketDeletedEvent(
    override val id: String,
) : BasketEvent
