package jp.inaba.service.application.query.order

data class OrderFindByUserQuery(val userId: String)

data class OrderFindByUserResult(
    val orderId: String,
    val userId: String,
    val orderStatus: String
)