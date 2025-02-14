package jp.inaba.service.infrastructure.projector.user

import jp.inaba.message.user.event.UserCreatedEvent
import jp.inaba.service.infrastructure.jpa.user.UserJpaEntity
import jp.inaba.service.infrastructure.jpa.user.UserJpaRepository
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventhandling.Timestamp
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Component
@ProcessingGroup(UserProjectorEventProcessor.PROCESSOR_NAME)
class UserProjector(
    private val repository: UserJpaRepository,
) {
    @EventHandler
    fun on(event: UserCreatedEvent, @Timestamp timestamp: Instant) {
        val entity =
            UserJpaEntity(
                id = event.id,
                userName = "ふｌえええつよい",
                createdAt = LocalDateTime.ofInstant(timestamp, ZoneId.of("Asia/Tokyo")),
                updatedAt = LocalDateTime.ofInstant(timestamp, ZoneId.of("Asia/Tokyo")),
            )

        repository.save(entity)
    }
}
