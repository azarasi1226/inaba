package jp.inaba.service.application.projector.brand

import jp.inaba.message.brand.event.BrandCreatedEvent
import jp.inaba.service.infrastructure.jpa.brand.BrandJpaEntity
import jp.inaba.service.infrastructure.jpa.brand.BrandJpaRepository
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventhandling.ResetHandler
import org.axonframework.eventhandling.Timestamp
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Component
@ProcessingGroup(BrandProjectorEventProcessor.PROCESSOR_NAME)
class BrandProjector(
    private val repository: BrandJpaRepository
) {
    @ResetHandler
    fun on() {
        repository.deleteAllInBatch()
    }

    @EventHandler
    fun on(event: BrandCreatedEvent, @Timestamp timestamp: Instant) {
        val entity = BrandJpaEntity(
            id = event.id,
            name = event.name,
            createdAt = LocalDateTime.ofInstant(timestamp, ZoneId.of("Asia/Tokyo")),
            updatedAt = LocalDateTime.ofInstant(timestamp, ZoneId.of("Asia/Tokyo")),
        )

        repository.save(entity)
    }
}