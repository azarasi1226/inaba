package jp.inaba.basket.api.domain.basket

import jp.inaba.catalog.api.domain.product.ProductId
import jp.inaba.identity.api.domain.user.UserId
import org.axonframework.commandhandling.RoutingKey
import org.axonframework.modelling.command.TargetAggregateIdentifier

interface BasketCommand {
    @get:RoutingKey
    val id: BasketId
}

interface BasketAggregateCommand {
    @get:TargetAggregateIdentifier
    val id: BasketId
}

data class CreateBasketCommand(
    override val id: BasketId,
    val userId: UserId,
) : BasketCommand

data class SetBasketItemCommand(
    override val id: BasketId,
    val productId: ProductId,
    val basketItemQuantity: BasketItemQuantity,
) : BasketCommand

data class DeleteBasketItemCommand(
    override val id: BasketId,
    val productId: ProductId,
) : BasketAggregateCommand

data class ClearBasketCommand(
    override val id: BasketId,
) : BasketAggregateCommand

data class DeleteBasketCommand(
    override val id: BasketId,
) : BasketAggregateCommand
