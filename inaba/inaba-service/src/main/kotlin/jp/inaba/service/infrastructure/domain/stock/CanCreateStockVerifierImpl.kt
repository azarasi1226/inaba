package jp.inaba.service.infrastructure.domain.stock

import jp.inaba.core.domain.product.ProductId
import jp.inaba.service.domain.stock.CanCreateStockVerifier
import jp.inaba.service.infrastructure.jpa.lookupproduct.LookupProductJpaRepository
import org.springframework.stereotype.Service

@Service
class CanCreateStockVerifierImpl(
    private val repository: LookupProductJpaRepository,
) : CanCreateStockVerifier {
    override fun isProductNotFound(productId: ProductId): Boolean {
        return repository.findById(productId.value).isEmpty
    }
}
