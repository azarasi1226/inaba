package jp.inaba.service.domain.product

import jp.inaba.core.domain.product.ProductId

interface CanCreateProductVerifier {
    fun isProductExists(productId: ProductId) : Boolean
}