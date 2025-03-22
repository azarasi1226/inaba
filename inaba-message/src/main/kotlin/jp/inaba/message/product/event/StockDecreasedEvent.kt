package jp.inaba.message.product.event

data class StockDecreasedEvent(
    val id: String,
    val idempotencyId: String,
    val decreaseStockQuantity: Int,
    val decreasedStockQuantity: Int,
)
