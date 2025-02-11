package jp.inaba.service.infrastructure.domain.stock

import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.stock.StockId
import jp.inaba.service.domain.stock.CanCreateStockVerifier
import jp.inaba.service.infrastructure.jpa.lookupproduct.LookupProductJpaRepository
import jp.inaba.service.infrastructure.jpa.lookupstock.LookupStockJpaRepository
import org.springframework.stereotype.Service

@Service
class CanCreateStockVerifierImpl(
    private val lookupProductJpaRepository: LookupProductJpaRepository,
    private val lookupStockJpaRepository: LookupStockJpaRepository,
) : CanCreateStockVerifier {
    override fun isStockExists(stockId: StockId): Boolean {
        return lookupStockJpaRepository.existsById(stockId.value)
    }

    override fun isProductNotFound(productId: ProductId): Boolean {
        return !lookupProductJpaRepository.existsById(productId.value)
    }

    override fun isLinkedProduct(productId: ProductId): Boolean {
        return lookupStockJpaRepository.existsByProductId(productId.value)
    }
}
