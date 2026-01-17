package jp.inaba.service.application.projector.usermetadata

import jp.inaba.message.basket.event.BasketCreatedEvent
import jp.inaba.message.user.event.UserCreatedEvent
import jp.inaba.service.infrastructure.jooq.generated.tables.references.USER_METADATA
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventhandling.ResetHandler
import org.jooq.DSLContext
import org.springframework.stereotype.Component

@Component
@ProcessingGroup(UserMetadataProjectorEventProcessor.PROCESSOR_NAME)
class UserMetadataProjector(
    private val dsl: DSLContext,
) {
    @ResetHandler
    fun reset() {
        dsl.deleteFrom(USER_METADATA).execute()
    }

    @EventHandler
    fun on(event: UserCreatedEvent) {
        dsl
            .insertInto(
                USER_METADATA,
                USER_METADATA.SUBJECT,
                USER_METADATA.USER_ID,
            ).values(
                event.subject,
                event.id,
            ).onDuplicateKeyIgnore()
            .execute()
    }

    @EventHandler
    fun on(event: BasketCreatedEvent) {
        dsl
            .update(USER_METADATA)
            .set(USER_METADATA.BASKET_ID, event.id)
            .where(USER_METADATA.USER_ID.eq(event.userId))
            .execute()
    }
}
