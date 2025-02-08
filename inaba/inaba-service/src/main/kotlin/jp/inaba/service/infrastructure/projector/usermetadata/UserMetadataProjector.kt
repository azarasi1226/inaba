package jp.inaba.service.infrastructure.projector.usermetadata

import jp.inaba.message.user.event.BasketIdLinkedEvent
import jp.inaba.message.user.event.SubjectLinkedEvent
import jp.inaba.service.infrastructure.jpa.usermetadata.UserMetadataJpaEntity
import jp.inaba.service.infrastructure.jpa.usermetadata.UserMetadataJpaRepository
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
@ProcessingGroup(UserMetadataProjectorEventProcessor.PROCESSOR_NAME)
class UserMetadataProjector(
    private val repository: UserMetadataJpaRepository,
) {
    @EventHandler
    fun on(event: SubjectLinkedEvent) {
        val entity = UserMetadataJpaEntity(
            subject = event.subject,
            userId = event.id,
        )

        repository.save(entity)
    }

    @EventHandler
    fun on(event: BasketIdLinkedEvent) {
        val entity = repository.findByUserId(event.id).orElseThrow()
        val updatedEntity = entity.copy(basketId = event.basketId)

        repository.save(updatedEntity)
    }
}