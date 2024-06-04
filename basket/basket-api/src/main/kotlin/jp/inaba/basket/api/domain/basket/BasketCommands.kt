package jp.inaba.basket.api.domain.basket

import jp.inaba.catalog.api.domain.product.ProductId
import jp.inaba.identity.api.domain.user.UserId
import org.axonframework.commandhandling.RoutingKey
import org.axonframework.modelling.command.TargetAggregateIdentifier

interface BasketAggregateCommand {
    @get:TargetAggregateIdentifier
    val id: BasketId
}

object BasketCommands {
    data class Create(
        @RoutingKey
        val userId: UserId,
    )

    data class SetBasketItem(
        override val id: BasketId,
        val productId: ProductId,
        val basketItemQuantity: BasketItemQuantity,
    ) : BasketAggregateCommand

    data class DeleteBasketItem(
        override val id: BasketId,
        val productId: ProductId,
    ) : BasketAggregateCommand

    data class Clear(
        override val id: BasketId,
    ) : BasketAggregateCommand
}
