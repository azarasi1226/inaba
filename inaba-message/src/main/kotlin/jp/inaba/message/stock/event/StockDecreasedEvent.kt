package jp.inaba.message.stock.event

data class StockDecreasedEvent(
    val id: String,
    val decreaseCount: Int,
    // 　冪等性対策
    val idempotencyId: String,
    val decreasedStockQuantity: Int,
)
