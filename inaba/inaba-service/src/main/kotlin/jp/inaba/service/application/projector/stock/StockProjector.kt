package jp.inaba.service.application.projector.stock

import jp.inaba.message.stock.event.StockCreatedEvent
import jp.inaba.message.stock.event.StockDecreasedEvent
import jp.inaba.message.stock.event.StockIncreasedEvent
import jp.inaba.service.infrastructure.jpa.stock.StockJpaEntity
import jp.inaba.service.infrastructure.jpa.stock.StockJpaRepository
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventhandling.ResetHandler
import org.springframework.stereotype.Component

@Component
@ProcessingGroup(StockProjectorEventProcessor.PROCESSOR_NAME)
class StockProjector(
    private val repository: StockJpaRepository
) {
    @ResetHandler
    fun reset() {
        repository.deleteAllInBatch()
    }

    @EventHandler
    fun on(event: StockCreatedEvent) {
        val entity =
            StockJpaEntity(
                id = event.id,
                productId = event.productId,
                quantity = 0,
            )

        repository.save(entity)
    }

    @EventHandler
    fun on(event: StockIncreasedEvent) {
        val entity = repository.findById(event.id).orElseThrow()
        val updatedEntity = entity.copy(quantity = event.increasedStockQuantity)

        repository.save(updatedEntity)
    }

    @EventHandler
    fun on(event: StockDecreasedEvent) {
        val entity = repository.findById(event.id).orElseThrow()
        val updatedEntity = entity.copy(quantity = event.decreasedStockQuantity)

        repository.save(updatedEntity)
    }
}
