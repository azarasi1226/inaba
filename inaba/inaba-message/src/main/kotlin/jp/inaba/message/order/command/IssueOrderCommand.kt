package jp.inaba.message.order.command

import jp.inaba.core.domain.order.OrderId
import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.stock.StockQuantity
import jp.inaba.core.domain.user.UserId

data class IssueOrderCommand(
    override val id: OrderId,
    val userId: UserId,
    val basketItems: List<BasketItem>,
) : OrderAggregateCommand {
    data class BasketItem(
        val productId: ProductId,
        val stockQuantity: StockQuantity,
    )
}
