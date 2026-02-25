package jp.inaba.message.product.event

data class StockDecreasedEvent(
    override val id: String,
    val idempotencyId: String,
    val decreaseStockQuantity: Int,
    val decreasedStockQuantity: Int,
): ProductEvent
