package jp.inaba.service.application.projector.brand

import jp.inaba.message.brand.event.BrandCreatedEvent
import jp.inaba.service.infrastructure.jpa.brand.BrandJpaEntity
import jp.inaba.service.infrastructure.jpa.brand.BrandJpaRepository
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
@ProcessingGroup(BrandProjectorEventProcessor.PROCESSOR_NAME)
class BrandProjector(
    private val repository: BrandJpaRepository
) {
    @EventHandler
    fun on(event: BrandCreatedEvent) {
        val entity = BrandJpaEntity(
            id = event.id,
            name = event.name
        )

        repository.save(entity)
    }
}