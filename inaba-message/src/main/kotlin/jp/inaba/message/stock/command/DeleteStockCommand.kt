package jp.inaba.message.stock.command

import jp.inaba.core.domain.stock.StockId

data class DeleteStockCommand(
    override val id: StockId,
) : StockAggregateCommand
