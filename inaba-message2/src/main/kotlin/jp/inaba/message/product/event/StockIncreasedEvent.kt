package jp.inaba.message.product.event

data class StockIncreasedEvent(
    val id: String,
    val idempotencyId: String,
    val increaseStockQuantity: Int,
    val increasedStockQuantity: Int,
)
