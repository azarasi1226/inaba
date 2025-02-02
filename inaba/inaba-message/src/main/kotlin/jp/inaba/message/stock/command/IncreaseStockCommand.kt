package jp.inaba.message.stock.command

import jp.inaba.core.domain.common.IdempotencyId
import jp.inaba.core.domain.stock.IncreaseCount
import jp.inaba.core.domain.stock.StockId

data class IncreaseStockCommand(
    override val id: StockId,
    val increaseCount: IncreaseCount,

    //　冪等性対策
    val idempotencyId: IdempotencyId,
) : StockAggregateCommand
