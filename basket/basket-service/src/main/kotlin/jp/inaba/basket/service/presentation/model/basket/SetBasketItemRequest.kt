package jp.inaba.basket.service.presentation.model.basket

data class SetBasketItemRequest(
    val productId: String,
    val itemQuantity: Int
)