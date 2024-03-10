package jp.inaba.basket.service.presentation.model.basket

data class SetItemRequest(
    val itemId: String,
    val itemQuantity: Int
)