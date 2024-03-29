package jp.inaba.basket.api.domain.basket

sealed interface BasketEvent {
    val id: String
}

object BasketEvents {
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