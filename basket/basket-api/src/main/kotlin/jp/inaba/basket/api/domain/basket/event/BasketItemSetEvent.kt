package jp.inaba.basket.api.domain.basket.event

data class BasketItemSetEvent(
    override val id: String,
    val productId: String,
    val basketItemQuantity: Int,
) : BasketEvent
