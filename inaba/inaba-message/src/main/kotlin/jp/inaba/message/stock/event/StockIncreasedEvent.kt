package jp.inaba.message.stock.event

data class StockIncreasedEvent(
    override val id: String,
    val idempotencyId: String,
    val increaseCount: Int,
) : StockEvent
