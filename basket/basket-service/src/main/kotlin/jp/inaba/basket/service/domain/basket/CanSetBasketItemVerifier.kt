package jp.inaba.basket.service.domain.basket

import jp.inaba.catalog.api.domain.product.ProductId

interface CanSetBasketItemVerifier {
    fun existProduct(productId: ProductId): Boolean
}