package jp.inaba.service.infrastructure.projector.user

import jp.inaba.message.user.event.UserCreatedEvent
import jp.inaba.service.infrastructure.jpa.user.UserJpaEntity
import jp.inaba.service.infrastructure.jpa.user.UserJpaRepository
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
@ProcessingGroup(UserProjectorEventProcessor.PROCESSOR_NAME)
class UserProjector(
    private val repository: UserJpaRepository,
) {
    @EventHandler
    fun on(event: UserCreatedEvent) {
        val entity =
            UserJpaEntity(
                id = event.id,
                userName = "ふｌえええ",
            )

        repository.save(entity)
    }
}
