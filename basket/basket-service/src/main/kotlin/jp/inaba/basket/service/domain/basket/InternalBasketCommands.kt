package jp.inaba.basket.service.domain.basket

import jp.inaba.basket.api.domain.basket.BasketAggregateCommand
import jp.inaba.basket.api.domain.basket.BasketId
import jp.inaba.basket.api.domain.basket.BasketItemQuantity
import jp.inaba.catalog.api.domain.product.ProductId

data class InternalCreateBasketCommand(
    override val id: BasketId,
) : BasketAggregateCommand

data class InternalSetBasketItemCommand(
    override val id: BasketId,
    val productId: ProductId,
    val basketItemQuantity: BasketItemQuantity,
) : BasketAggregateCommand
