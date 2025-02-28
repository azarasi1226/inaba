package jp.inaba.service.infrastructure.domain.stock

import jp.inaba.core.domain.product.ProductId
import jp.inaba.service.domain.stock.CreateStockVerifier
import jp.inaba.service.infrastructure.jpa.lookupproduct.LookupProductJpaRepository
import jp.inaba.service.infrastructure.jpa.lookupstock.LookupStockJpaRepository
import org.springframework.stereotype.Service

@Service
class CreateStockVerifierImpl(
    private val lookupProductJpaRepository: LookupProductJpaRepository,
    private val lookupStockJpaRepository: LookupStockJpaRepository,
) : CreateStockVerifier {
    override fun isProductNotFound(productId: ProductId): Boolean {
        return !lookupProductJpaRepository.existsById(productId.value)
    }

    override fun isLinkedProduct(productId: ProductId): Boolean {
        return lookupStockJpaRepository.existsByProductId(productId.value)
    }
}
