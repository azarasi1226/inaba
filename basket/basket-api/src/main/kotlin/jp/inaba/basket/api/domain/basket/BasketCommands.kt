package jp.inaba.basket.api.domain.basket

import jp.inaba.catalog.api.domain.product.ProductId
import jp.inaba.identity.api.domain.user.UserId
import org.axonframework.commandhandling.RoutingKey
import org.axonframework.modelling.command.TargetAggregateIdentifier

interface BasketAggregateCommand {
    @get:TargetAggregateIdentifier
    val id: BasketId
}

data class CreateBasketCommand(
    @RoutingKey
    val userId: UserId,
)

data class SetBasketItemCommand(
    @RoutingKey
    val id: BasketId,
    val productId: ProductId,
    val basketItemQuantity: BasketItemQuantity,
)

data class DeleteBasketItemCommand(
    override val id: BasketId,
    val productId: ProductId,
) : BasketAggregateCommand

data class ClearBasketCommand(
    override val id: BasketId,
) : BasketAggregateCommand
