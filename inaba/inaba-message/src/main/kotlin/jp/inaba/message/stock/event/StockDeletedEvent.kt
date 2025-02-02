package jp.inaba.message.stock.event

data class StockDeletedEvent(
    override val id: String
) : StockEvent