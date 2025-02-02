package jp.inaba.service.infrastructure.projector.lookupproduct

import jp.inaba.message.basket.event.BasketDeletedEvent
import jp.inaba.message.product.event.ProductCreatedEvent
import jp.inaba.service.infrastructure.jpa.lookupproduct.LookupProductJpaEntity
import jp.inaba.service.infrastructure.jpa.lookupproduct.LookupProductJpaRepository
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
@ProcessingGroup(LookupProductProjectorEventProcessor.PROCESSOR_NAME)
class LookupProductProjector(
    private val lookupProductJpaRepository: LookupProductJpaRepository,
) {
    @EventHandler
    fun on(event: ProductCreatedEvent) {
        val entity =
            LookupProductJpaEntity(
            id = event.id,
            name = event.name,
        )

        lookupProductJpaRepository.save(entity)
    }

    @EventHandler
    fun on(event: BasketDeletedEvent) {
        lookupProductJpaRepository.deleteById(event.id)
    }
}