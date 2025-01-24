package jp.inaba.message.stock.command

import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.stock.StockId
import org.axonframework.commandhandling.RoutingKey

data class CreateStockCommand(
    @get:RoutingKey
    val id: StockId,
    val productId: ProductId,
)
