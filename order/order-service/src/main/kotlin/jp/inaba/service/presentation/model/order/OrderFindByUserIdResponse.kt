package jp.inaba.service.presentation.model.order

data class OrderFindByUserIdResponse(
    val orderId: String,
    val userId: String,
    val status: String,
)
