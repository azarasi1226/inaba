package jp.inaba.message.basket.event

data class BasketItemSetEvent(
    val id: String,
    val productId: String,
    val basketItemQuantity: Int,
)
