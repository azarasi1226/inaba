package jp.inaba.message.basket.command

import jp.inaba.core.domain.basket.BasketId
import jp.inaba.core.domain.product.ProductId

data class DeleteBasketItemCommand(
    override val id: BasketId,
    val productId: ProductId,
) : BasketAggregateCommand
