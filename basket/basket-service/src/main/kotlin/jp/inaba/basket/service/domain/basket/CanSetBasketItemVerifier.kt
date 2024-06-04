package jp.inaba.basket.service.domain.basket

import com.github.michaelbull.result.Result
import jp.inaba.basket.api.domain.basket.BasketErrors
import jp.inaba.catalog.api.domain.product.ProductId

interface CanSetBasketItemVerifier {
    fun checkProductExits(productId: ProductId): Result<Unit, BasketErrors.SetBasketItem>
}
