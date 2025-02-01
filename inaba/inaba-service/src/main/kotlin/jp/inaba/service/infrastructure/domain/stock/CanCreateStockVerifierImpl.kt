package jp.inaba.service.infrastructure.domain.stock

import jp.inaba.core.domain.product.ProductId
import jp.inaba.service.domain.stock.CanCreateStockVerifier
import jp.inaba.service.infrastructure.jpa.product.ProductJpaRepository
import org.springframework.stereotype.Service

@Service
class CanCreateStockVerifierImpl(
    private val productJpaRepository: ProductJpaRepository,
) : CanCreateStockVerifier {
    override fun isProductNotFound(productId: ProductId): Boolean {
        return !productJpaRepository.existsById(productId.value)
    }
}
