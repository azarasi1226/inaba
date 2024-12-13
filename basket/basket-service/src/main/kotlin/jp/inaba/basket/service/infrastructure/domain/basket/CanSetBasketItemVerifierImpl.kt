package jp.inaba.basket.service.infrastructure.domain.basket

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import jp.inaba.basket.share.domain.basket.SetBasketItemError
import jp.inaba.basket.service.domain.basket.CanSetBasketItemVerifier
import jp.inaba.basket.service.infrastructure.jpa.product.ProductJpaRepository
import jp.inaba.catalog.api.domain.product.ProductId
import org.springframework.stereotype.Service

@Service
class CanSetBasketItemVerifierImpl(
    private val productJpaRepository: ProductJpaRepository,
) : CanSetBasketItemVerifier {
    override fun checkProductExits(productId: ProductId): Result<Unit, SetBasketItemError> {
        return if (productJpaRepository.existsById(productId.value)) {
            Ok(Unit)
        } else {
            Err(SetBasketItemError.PRODUCT_NOT_FOUND)
        }
    }
}
