package jp.inaba.service.infrastructure.domain.product

import jp.inaba.core.domain.brand.BrandId
import jp.inaba.core.domain.product.ProductId
import jp.inaba.service.domain.product.CreateProductVerifier
import jp.inaba.service.infrastructure.jpa.lookupbrand.LookupBrandJpaRepository
import org.axonframework.eventsourcing.eventstore.EventStore
import org.springframework.stereotype.Service

@Service
class CreateProductVerifierImpl(
    private val eventStore: EventStore,
    private val lookupBrandJpaRepository: LookupBrandJpaRepository
) : CreateProductVerifier {
    override fun isProductExists(productId: ProductId): Boolean {
        return eventStore.lastSequenceNumberFor(productId.value).isPresent
    }

    override fun isBrandNotFound(brandId: BrandId): Boolean {
        return lookupBrandJpaRepository.findById(brandId.value).isEmpty
    }
}
