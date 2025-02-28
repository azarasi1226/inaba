package jp.inaba.service.application.projector.lookupstock

import jp.inaba.message.stock.event.StockCreatedEvent
import jp.inaba.message.stock.event.StockDeletedEvent
import jp.inaba.service.infrastructure.jpa.lookupstock.LookupStockJpaEntity
import jp.inaba.service.infrastructure.jpa.lookupstock.LookupStockJpaRepository
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
@ProcessingGroup(LookupStockProjectorEventProcessor.PROCESSOR_NAME)
class LookupStockProjector(
    private val repository: LookupStockJpaRepository,
) {
    @EventHandler
    fun on(event: StockCreatedEvent) {
        val entity =
            LookupStockJpaEntity(
                id = event.id,
                productId = event.productId,
            )

        repository.save(entity)
    }

    @EventHandler
    fun on(event: StockDeletedEvent) {
        repository.deleteById(event.id)
    }
}
