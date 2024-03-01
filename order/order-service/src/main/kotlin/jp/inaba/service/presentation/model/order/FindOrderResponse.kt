package jp.inaba.service.presentation.model.order

data class FindOrderResponse (
    val orderId: String,
    val userId: String,
    val status: String
)