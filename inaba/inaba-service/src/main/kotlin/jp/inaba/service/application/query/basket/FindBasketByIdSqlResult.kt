package jp.inaba.service.application.query.basket

class FindBasketByIdSqlResult(
    val productId: String,
    val productName: String,
    val productPrice: Int,
    val productPictureUrl: String,
    val quantity: Int,
    val totalCount: Long,
)
