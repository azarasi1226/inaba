package jp.inaba.service.domain.stock

import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.stock.StockId

interface CreateStockVerifier {
    fun isStockExists(stockId: StockId): Boolean

    fun isProductNotFound(productId: ProductId): Boolean

    fun isLinkedProduct(productId: ProductId): Boolean
}
