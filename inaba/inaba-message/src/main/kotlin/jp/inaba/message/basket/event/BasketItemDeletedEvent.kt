package jp.inaba.message.basket.event

data class BasketItemDeletedEvent(
    override val id: String,
    val productId: String,
) : BasketEvent
