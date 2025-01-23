package jp.inaba.message.stock.command

import jp.inaba.core.domain.common.IdempotencyId
import jp.inaba.core.domain.stock.StockId
import jp.inaba.core.domain.stock.StockQuantity

data class IncreaseStockCommand(
    override val id: StockId,
    val idempotencyId: IdempotencyId,
    val increaseCount: StockQuantity,
) : StockAggregateCommand
