package jp.inaba.service.domain.basket

import com.github.michaelbull.result.Result
import jp.inaba.core.domain.basket.SetBasketItemError
import jp.inaba.core.domain.product.ProductId

interface CanSetBasketItemVerifier {
    fun checkProductExits(productId: ProductId): Result<Unit, SetBasketItemError>
}
