package jp.inaba.service.domain.stock

import jp.inaba.core.domain.product.ProductId

interface CanCreateStockVerifier {
    fun isProductNotFound(productId: ProductId): Boolean
}
