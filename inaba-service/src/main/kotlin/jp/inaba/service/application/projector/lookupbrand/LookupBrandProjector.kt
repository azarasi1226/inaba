package jp.inaba.service.application.projector.lookupbrand

import jp.inaba.message.brand.event.BrandCreatedEvent
import jp.inaba.message.brand.event.BrandDeletedEvent
import jp.inaba.service.infrastructure.jpa.lookupbrand.LookupBrandJpaEntity
import jp.inaba.service.infrastructure.jpa.lookupbrand.LookupBrandJpaRepository
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
@ProcessingGroup(LookupBrandProjectorEventProcessor.PROCESSOR_NAME)
class LookupBrandProjector(
    private val repository: LookupBrandJpaRepository,
) {
    @EventHandler
    fun on(event: BrandCreatedEvent) {
        val entity = LookupBrandJpaEntity(event.id)

        repository.save(entity)
    }

    @EventHandler
    fun on(event: BrandDeletedEvent) {
        repository.deleteById(event.id)
    }
}
