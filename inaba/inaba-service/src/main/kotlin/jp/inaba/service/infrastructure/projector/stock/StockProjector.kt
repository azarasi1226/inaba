package jp.inaba.service.infrastructure.projector.stock

import jp.inaba.message.stock.event.StockCreatedEvent
import jp.inaba.message.stock.event.StockDecreasedEvent
import jp.inaba.message.stock.event.StockIncreasedEvent
import jp.inaba.service.infrastructure.jpa.product.ProductJpaRepository
import jp.inaba.service.infrastructure.jpa.stock.StockJpaEntity
import jp.inaba.service.infrastructure.jpa.stock.StockJpaRepository
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
@ProcessingGroup(StockProjectorEventProcessor.PROCESSOR_NAME)
class StockProjector(
    private val stockJpaRepository: StockJpaRepository,
    private val productJpaRepository: ProductJpaRepository,
) {
    @EventHandler
    fun on(event: StockCreatedEvent) {
        val product = productJpaRepository.findById(event.productId).orElseThrow()

        val entity =
            StockJpaEntity(
                id = event.id,
                product = product,
                quantity = 0,
            )

        stockJpaRepository.save(entity)
    }

    @EventHandler
    fun on(event: StockIncreasedEvent) {
        val entity = stockJpaRepository.findById(event.id).orElseThrow()

        val updatedEntity = entity.copy(quantity = event.increasedStockQuantity)

        stockJpaRepository.save(updatedEntity)
    }

    @EventHandler
    fun on(event: StockDecreasedEvent) {
        val entity = stockJpaRepository.findById(event.id).orElseThrow()

        val updatedEntity = entity.copy(quantity = event.decreasedStockQuantity)

        stockJpaRepository.save(updatedEntity)
    }
}
