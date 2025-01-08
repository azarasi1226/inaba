package jp.inaba.message.basket.event

data class BasketDeletedEvent(
    override val id: String,
) : BasketEvent
