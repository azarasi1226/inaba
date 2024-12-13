package jp.inaba.basket.service.domain.basket

import jp.inaba.basket.api.domain.basket.command.BasketAggregateCommand
import jp.inaba.basket.share.domain.basket.BasketId
import jp.inaba.basket.share.domain.basket.BasketItemQuantity
import jp.inaba.catalog.api.domain.product.ProductId
import jp.inaba.identity.share.domain.user.UserId

data class InternalCreateBasketCommand(
    override val id: BasketId,
    val userId: UserId,
) : BasketAggregateCommand

data class InternalSetBasketItemCommand(
    override val id: BasketId,
    val productId: ProductId,
    val basketItemQuantity: BasketItemQuantity,
) : BasketAggregateCommand
