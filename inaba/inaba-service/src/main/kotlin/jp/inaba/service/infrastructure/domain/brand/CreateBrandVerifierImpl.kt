package jp.inaba.service.infrastructure.domain.brand

import jp.inaba.core.domain.brand.BrandId
import jp.inaba.service.domain.brand.CreateBrandVerifier
import org.axonframework.eventsourcing.eventstore.EventStore
import org.springframework.stereotype.Service

@Service
class CreateBrandVerifierImpl(
    private val eventStore: EventStore,
) : CreateBrandVerifier {
    override fun isBrandExits(brandId: BrandId): Boolean {
        return eventStore.lastSequenceNumberFor(brandId.value).isPresent
    }
}
