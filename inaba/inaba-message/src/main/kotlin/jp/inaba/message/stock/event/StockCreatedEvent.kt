package jp.inaba.message.stock.event

data class StockCreatedEvent(
    override val id: String,
    val productId: String,
) : StockEvent