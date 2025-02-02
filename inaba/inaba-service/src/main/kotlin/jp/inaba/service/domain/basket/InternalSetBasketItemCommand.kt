package jp.inaba.service.domain.basket

import jp.inaba.core.domain.basket.BasketId
import jp.inaba.core.domain.basket.BasketItemQuantity
import jp.inaba.core.domain.product.ProductId
import jp.inaba.message.basket.command.BasketAggregateCommand

data class InternalSetBasketItemCommand(
    override val id: BasketId,
    val productId: ProductId,
    val basketItemQuantity: BasketItemQuantity,
) : BasketAggregateCommand
