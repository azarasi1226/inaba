package jp.inaba.basket.service.application.command.basket.setproduct

import jp.inaba.basket.api.domain.basket.BasketId
import jp.inaba.basket.api.domain.basket.BasketItemQuantity
import jp.inaba.catalog.api.domain.product.ProductId

data class SetBasketItemInputData(
    val basketId: BasketId,
    val productId: ProductId,
    val basketItemQuantity: BasketItemQuantity
)