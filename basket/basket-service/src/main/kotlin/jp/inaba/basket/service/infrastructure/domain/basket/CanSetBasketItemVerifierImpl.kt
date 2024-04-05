package jp.inaba.basket.service.infrastructure.domain.basket

import jp.inaba.basket.service.domain.basket.CanSetBasketItemVerifier
import jp.inaba.basket.service.infrastructure.jpa.product.ProductJpaRepository
import jp.inaba.catalog.api.domain.product.ProductId
import org.springframework.stereotype.Service

@Service
class CanSetBasketItemVerifierImpl(
    private val productJpaRepository: ProductJpaRepository
): CanSetBasketItemVerifier{
    override fun existProduct(productId: ProductId): Boolean {
        return productJpaRepository.existsById(productId.value)
    }
}