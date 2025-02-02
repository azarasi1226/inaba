package jp.inaba.service.infrastructure.domain.basket

import jp.inaba.core.domain.product.ProductId
import jp.inaba.service.domain.basket.CanSetBasketItemVerifier
import jp.inaba.service.infrastructure.jpa.lookupproduct.LookupProductJpaRepository
import org.springframework.stereotype.Service

@Service
class CanSetBasketItemVerifierImpl(
    private val lookupProductJpaRepository: LookupProductJpaRepository,
) : CanSetBasketItemVerifier {
    override fun isProductNotFound(productId: ProductId): Boolean {
        return !lookupProductJpaRepository.existsById(productId.value)
    }
}
