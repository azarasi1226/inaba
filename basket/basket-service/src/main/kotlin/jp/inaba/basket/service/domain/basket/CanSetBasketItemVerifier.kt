package jp.inaba.basket.service.domain.basket

import com.github.michaelbull.result.Result
import jp.inaba.basket.share.domain.basket.SetBasketItemError
import jp.inaba.catalog.api.domain.product.ProductId

interface CanSetBasketItemVerifier {
    fun checkProductExits(productId: ProductId): Result<Unit, SetBasketItemError>
}
