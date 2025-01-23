package jp.inaba.message.stock.command

import jp.inaba.core.domain.common.IdempotencyId
import jp.inaba.core.domain.stock.StockId
import jp.inaba.core.domain.stock.StockQuantity

data class DecreaseStockCommand(
    override val id: StockId,
    val idempotencyId: IdempotencyId,
    val decreaseCount: StockQuantity,
) : StockAggregateCommand
