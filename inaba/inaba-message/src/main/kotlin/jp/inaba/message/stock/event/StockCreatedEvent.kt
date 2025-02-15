package jp.inaba.message.stock.event

data class StockCreatedEvent(
    val id: String,
    val productId: String,
)
