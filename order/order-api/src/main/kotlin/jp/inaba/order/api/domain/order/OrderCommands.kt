package jp.inaba.order.api.domain.order

import jp.inaba.catalog.api.domain.product.ProductId
import jp.inaba.catalog.api.domain.product.ProductQuantity
import jp.inaba.identity.api.domain.user.UserId
import org.axonframework.modelling.command.TargetAggregateIdentifier

interface OrderCommand {
    @get:TargetAggregateIdentifier
    val id: OrderId
}

data class IssueOrderCommand(
    override val id: OrderId,
    val userId: UserId,
    val basketItems: List<BasketItem>,
) : OrderCommand {
    data class BasketItem(
        val productId: ProductId,
        val productQuantity: ProductQuantity,
    )
}

data class SecureBasketItemCommand(
    override val id: OrderId,
    val secureBasketItemStatuses: List<SecureBasketItemStatus>,
) : OrderCommand {
    data class SecureBasketItemStatus(
        val productId: ProductId,
        val status: BasketItemStatus,
    )

    enum class BasketItemStatus {
        SUCCESS,
        FAILED,
    }
}

data class CompleteOrderCommand(override val id: OrderId) : OrderCommand

data class FailOrderCommand(override val id: OrderId) : OrderCommand
