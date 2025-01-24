package jp.inaba.message.stock.event

data class StockDecreasedEvent(
    override val id: String,
    val idempotencyId: String,
    val decreaseCount: Int,
) : StockEvent
