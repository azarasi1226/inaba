package jp.inaba.service.domain.stock

import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.stock.StockId
import jp.inaba.message.stock.command.StockAggregateCommand

data class InternalCreateStockCommand(
    override val id: StockId,
    val productId: ProductId,
) : StockAggregateCommand
