package jp.inaba.service.application.query.order

data class OrderFindByUserIdQuery(val userId: String)

data class OrderFindByUserIdResult(
    val orderId: String,
    val userId: String,
    val orderStatus: String,
)
