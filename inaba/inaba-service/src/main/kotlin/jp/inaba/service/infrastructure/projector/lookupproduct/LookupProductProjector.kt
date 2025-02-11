package jp.inaba.service.infrastructure.projector.lookupproduct

import jp.inaba.message.product.event.ProductCreatedEvent
import jp.inaba.message.product.event.ProductDeletedEvent
import jp.inaba.service.infrastructure.jpa.lookupproduct.LookupProductJpaEntity
import jp.inaba.service.infrastructure.jpa.lookupproduct.LookupProductJpaRepository
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
@ProcessingGroup(LookupProductProjectorEventProcessor.PROCESSOR_NAME)
class LookupProductProjector(
    private val repository: LookupProductJpaRepository,
) {
    @EventHandler
    fun on(event: ProductCreatedEvent) {
        val entity =
            LookupProductJpaEntity(
                id = event.id,
            )

        repository.save(entity)
    }

    @EventHandler
    fun on(event: ProductDeletedEvent) {
        repository.deleteById(event.id)
    }
}
