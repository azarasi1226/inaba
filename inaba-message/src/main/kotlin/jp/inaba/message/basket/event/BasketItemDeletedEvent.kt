package jp.inaba.message.basket.event

data class BasketItemDeletedEvent(
    val id: String,
    val productId: String,
)
