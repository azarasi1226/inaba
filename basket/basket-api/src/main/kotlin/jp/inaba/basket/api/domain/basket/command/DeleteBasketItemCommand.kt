package jp.inaba.basket.api.domain.basket.command

import jp.inaba.basket.share.domain.basket.BasketId
import jp.inaba.catalog.api.domain.product.ProductId

data class DeleteBasketItemCommand(
    override val id: BasketId,
    val productId: ProductId,
) : BasketAggregateCommand