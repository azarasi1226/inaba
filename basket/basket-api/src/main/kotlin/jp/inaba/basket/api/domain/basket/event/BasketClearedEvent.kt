package jp.inaba.basket.api.domain.basket.event

data class BasketClearedEvent(
    override val id: String,
) : BasketEvent