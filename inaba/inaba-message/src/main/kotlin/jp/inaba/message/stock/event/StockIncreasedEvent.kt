package jp.inaba.message.stock.event

data class StockIncreasedEvent(
    val id: String,
    val increaseCount: Int,
    // 　冪等性対策
    val idempotencyId: String,
    val increasedStockQuantity: Int,
)
