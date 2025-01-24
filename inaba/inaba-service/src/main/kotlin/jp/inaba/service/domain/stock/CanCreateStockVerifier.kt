package jp.inaba.service.domain.stock

import com.github.michaelbull.result.Result
import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.stock.CreateStockError

interface CanCreateStockVerifier {
    fun checkProductExits(productId: ProductId): Result<Unit, CreateStockError>
}
