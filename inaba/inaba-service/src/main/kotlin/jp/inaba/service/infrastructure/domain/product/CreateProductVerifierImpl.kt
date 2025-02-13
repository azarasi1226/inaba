package jp.inaba.service.infrastructure.domain.product

import jp.inaba.core.domain.product.ProductId
import jp.inaba.service.domain.product.CreateProductVerifier
import jp.inaba.service.infrastructure.jpa.lookupproduct.LookupProductJpaRepository
import org.springframework.stereotype.Service

@Service
class CreateProductVerifierImpl(
    private val repository: LookupProductJpaRepository,
) : CreateProductVerifier {
    override fun isProductExists(productId: ProductId): Boolean {
        return repository.existsById(productId.value)
    }
}
