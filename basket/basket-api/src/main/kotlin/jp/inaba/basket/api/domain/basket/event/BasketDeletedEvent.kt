package jp.inaba.basket.api.domain.basket.event

data class BasketDeletedEvent(
    override val id: String,
) : BasketEvent