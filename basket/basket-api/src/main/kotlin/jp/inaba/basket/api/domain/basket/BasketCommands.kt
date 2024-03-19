package jp.inaba.basket.api.domain.basket

import jp.inaba.catalog.api.domain.product.ProductId
import org.axonframework.modelling.command.TargetAggregateIdentifier

sealed interface BasketCommand {
    @get:TargetAggregateIdentifier
    val id: BasketId
}

object BasketCommands {
    data class Create(
        override val id: BasketId,
        //TODO(UserIdを値オブジェクトにする)
        val userId: String
    ) : BasketCommand

    data class SetBasketItem(
        override val id: BasketId,
        val productId: ProductId,
        val basketItemQuantity: BasketItemQuantity
    ) : BasketCommand

    data class DeleteBasketItem(
        override val id: BasketId,
        val productId: ProductId
    ) : BasketCommand

    data class Clear(
        override val id: BasketId
    ) : BasketCommand
}