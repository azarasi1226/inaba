package jp.inaba.basket.api.domain.basket.event

data class BasketCreatedEvent(
    override val id: String,
    val userId: String,
) : BasketEvent
