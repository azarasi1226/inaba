package jp.inaba.message.stock.command

import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.stock.StockId

data class CreateStockCommand(
    override val id: StockId,
    val productId: ProductId,
) : StockAggregateCommand