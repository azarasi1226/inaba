package jp.inaba.service.domain.basket

import jp.inaba.core.domain.product.ProductId

interface CanSetBasketItemVerifier {
    fun isProductNotFound(productId: ProductId): Boolean
}
