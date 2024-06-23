package jp.inaba.order.api.domain.order

import jp.inaba.catalog.api.domain.product.ProductId
import jp.inaba.catalog.api.domain.product.ProductQuantity

interface OrderEvent {
    val id: String
}

data class OrderIssuedEvent(
    override val id: String,
    val userId: String,
    val basketItems: List<BasketItem>
) : OrderEvent {
    data class BasketItem(
        val productId: String,
        val productQuantity: Int
    )
}

data class OrderCompletedEvent(
    override val id: String
) : OrderEvent

data class OrderFailedEvent(
    override val id: String
) : OrderEvent
