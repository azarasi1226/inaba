package jp.inaba.message.basket.event

data class BasketCreatedEvent(
    override val id: String,
    val userId: String,
) : BasketEvent
