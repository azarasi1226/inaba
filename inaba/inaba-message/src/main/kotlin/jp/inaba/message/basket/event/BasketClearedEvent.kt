package jp.inaba.message.basket.event

data class BasketClearedEvent(
    override val id: String,
) : BasketEvent
