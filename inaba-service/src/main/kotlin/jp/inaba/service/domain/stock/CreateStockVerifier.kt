package jp.inaba.service.domain.stock

import jp.inaba.core.domain.product.ProductId

interface CreateStockVerifier {
    fun isProductNotFound(productId: ProductId): Boolean

    fun isLinkedProduct(productId: ProductId): Boolean
}
