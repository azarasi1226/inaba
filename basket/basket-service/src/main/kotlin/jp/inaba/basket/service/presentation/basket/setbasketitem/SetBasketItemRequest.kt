package jp.inaba.basket.service.presentation.basket.setbasketitem

data class SetBasketItemRequest(
    val productId: String,
    val itemQuantity: Int
)