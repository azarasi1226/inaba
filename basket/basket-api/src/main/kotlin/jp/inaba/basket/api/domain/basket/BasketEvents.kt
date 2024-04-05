package jp.inaba.basket.api.domain.basket

interface BasketEvent {
    val id: String
}

object BasketEvents {
    data class Created(
        override val id: String
    ) : BasketEvent
    data class BasketItemSet(
        override val id: String,
        val productId: String,
        val basketItemQuantity: Int
    ) : BasketEvent

    data class BasketItemDeleted(
        override val id: String,
        val productId: String
    ) : BasketEvent

    data class Cleared(
        override val id: String
    ) : BasketEvent
}