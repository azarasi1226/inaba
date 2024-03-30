package jp.inaba.basket.service.application.query.basket.findbyid

class BasketFindByIdSqlResult(
    val itemId: String,
    val itemName: String,
    val itemPrice: Int,
    val itemPictureUrl: String,
    val itemQuantity: Int,
    val totalCount: Long
)