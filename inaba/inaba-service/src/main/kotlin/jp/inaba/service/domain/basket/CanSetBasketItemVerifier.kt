package jp.inaba.service.domain.basket

import jp.inaba.core.domain.basket.BasketItemQuantity
import jp.inaba.core.domain.product.ProductId

interface CanSetBasketItemVerifier {
    fun isProductNotFound(productId: ProductId): Boolean

    fun isOutOfStock(
        productId: ProductId,
        basketItemQuantity: BasketItemQuantity,
    ): Boolean
}
