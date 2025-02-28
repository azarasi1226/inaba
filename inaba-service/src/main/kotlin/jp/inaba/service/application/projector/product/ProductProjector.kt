package jp.inaba.service.application.projector.product

import jp.inaba.message.product.event.ProductCreatedEvent
import jp.inaba.message.product.event.ProductDeletedEvent
import jp.inaba.message.product.event.ProductUpdatedEvent
import jp.inaba.service.infrastructure.jpa.product.ProductJpaEntity
import jp.inaba.service.infrastructure.jpa.product.ProductJpaRepository
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventhandling.ResetHandler
import org.axonframework.eventhandling.Timestamp
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Component
@ProcessingGroup(ProductProjectorEventProcessor.PROCESSOR_NAME)
class ProductProjector(
    private val repository: ProductJpaRepository,
) {
    @ResetHandler
    fun reset() {
        repository.deleteAllInBatch()
    }

    @EventHandler
    fun on(
        event: ProductCreatedEvent,
        @Timestamp timestamp: Instant,
    ) {
        val entity =
            ProductJpaEntity(
                id = event.id,
                brandId = event.brandId,
                name = event.name,
                description = event.description,
                imageUrl = event.imageUrl,
                price = event.price,
                createdAt = LocalDateTime.ofInstant(timestamp, ZoneId.of("Asia/Tokyo")),
                updatedAt = LocalDateTime.ofInstant(timestamp, ZoneId.of("Asia/Tokyo")),
            )

        repository.save(entity)
    }

    @EventHandler
    fun on(
        event: ProductUpdatedEvent,
        @Timestamp timestamp: Instant,
    ) {
        val entity = repository.findById(event.id).orElseThrow()
        val updatedEntity =
            entity.copy(
                name = event.name,
                description = event.description,
                imageUrl = event.imageUrl,
                price = event.price,
                updatedAt = LocalDateTime.ofInstant(timestamp, ZoneId.of("Asia/Tokyo")),
            )

        repository.save(updatedEntity)
    }

    @EventHandler
    fun on(event: ProductDeletedEvent) {
        repository.deleteById(event.id)
    }
}
