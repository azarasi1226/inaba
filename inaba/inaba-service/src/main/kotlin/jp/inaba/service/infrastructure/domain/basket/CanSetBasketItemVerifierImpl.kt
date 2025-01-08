package jp.inaba.service.infrastructure.domain.basket

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import jp.inaba.core.domain.basket.SetBasketItemError
import jp.inaba.core.domain.product.ProductId
import jp.inaba.service.domain.basket.CanSetBasketItemVerifier
import jp.inaba.service.infrastructure.jpa.product.ProductJpaRepository
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
