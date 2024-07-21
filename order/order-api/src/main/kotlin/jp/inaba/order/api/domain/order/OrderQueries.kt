package jp.inaba.order.api.domain.order

data class OrderFindByUserIdQuery(
    val userId: String,
)

data class OrderFindByUserIdResult(
    val orderId: String,
    val userId: String,
    val orderStatus: String,
)
