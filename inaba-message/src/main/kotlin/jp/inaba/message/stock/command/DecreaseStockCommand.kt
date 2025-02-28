package jp.inaba.message.stock.command

import jp.inaba.core.domain.common.IdempotencyId
import jp.inaba.core.domain.stock.DecreaseCount
import jp.inaba.core.domain.stock.StockId

data class DecreaseStockCommand(
    override val id: StockId,
    val decreaseCount: DecreaseCount,
    // 　冪等性対策
    val idempotencyId: IdempotencyId,
) : StockAggregateCommand
