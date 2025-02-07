package jp.inaba.service.infrastructure.projector.lookupuser

import jp.inaba.message.user.event.UserCreatedEvent
import jp.inaba.message.user.event.UserDeletedEvent
import jp.inaba.service.infrastructure.jpa.lookupuser.LookupUserJpaEntity
import jp.inaba.service.infrastructure.jpa.lookupuser.LookupUserJpaRepository
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
@ProcessingGroup(LookupUserProjectorEventProcessor.PROCESSOR_NAME)
class LookupUserProjector(
    private val repository: LookupUserJpaRepository,
) {
    @EventHandler
    fun on(event: UserCreatedEvent) {
        val entity =
            LookupUserJpaEntity(
                id = event.id,
            )

        repository.save(entity)
    }

    @EventHandler
    fun on(event: UserDeletedEvent) {
        repository.deleteById(event.id)
    }
}
