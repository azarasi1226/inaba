package jp.inaba.message.order.command

import jp.inaba.core.domain.order.OrderId
import jp.inaba.core.domain.product.ProductId

data class SecureBasketItemCommand(
    override val id: OrderId,
    val secureBasketItemStatuses: List<SecureBasketItemStatus>,
) : OrderAggregateCommand {
    data class SecureBasketItemStatus(
        val productId: ProductId,
        val status: BasketItemStatus,
    )

    enum class BasketItemStatus {
        SUCCESS,
        FAILED,
    }
}
