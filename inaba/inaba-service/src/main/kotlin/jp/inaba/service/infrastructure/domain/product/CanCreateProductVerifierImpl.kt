package jp.inaba.service.infrastructure.domain.product

import jp.inaba.core.domain.product.ProductId
import jp.inaba.service.domain.product.CanCreateProductVerifier
import jp.inaba.service.infrastructure.jpa.lookupproduct.LookupProductJpaRepository
import org.springframework.stereotype.Service

@Service
class CanCreateProductVerifierImpl(
    private val repository: LookupProductJpaRepository,
) : CanCreateProductVerifier {
    override fun isProductExists(productId: ProductId): Boolean {
        return repository.existsById(productId.value)
    }
}
