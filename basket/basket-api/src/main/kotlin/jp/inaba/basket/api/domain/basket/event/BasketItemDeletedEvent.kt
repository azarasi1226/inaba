package jp.inaba.basket.api.domain.basket.event

data class BasketItemDeletedEvent(
    override val id: String,
    val productId: String,
) : BasketEvent
