package jp.inaba.basket.service.application.query.basket.findbyid

class FindBasketByIdSqlResult(
    val productId: String,
    val productName: String,
    val productPrice: Int,
    val productPictureUrl: String,
    val quantity: Int,
    val totalCount: Long,
)
