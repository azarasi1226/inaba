package jp.inaba.service.application.projector.lookupuser

import jp.inaba.message.user.event.UserCreatedEvent
import jp.inaba.message.user.event.UserDeletedEvent
import jp.inaba.service.infrastructure.jooq.generated.tables.references.LOOKUP_USER
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.jooq.DSLContext
import org.springframework.stereotype.Component

@Component
@ProcessingGroup(LookupUserProjectorEventProcessor.PROCESSOR_NAME)
class LookupUserProjector(
    private val dsl: DSLContext,
) {
    @EventHandler
    fun on(event: UserCreatedEvent) {
        dsl
            .insertInto(
                LOOKUP_USER,
                LOOKUP_USER.ID,
                LOOKUP_USER.SUBJECT,
            ).values(
                event.id,
                event.subject,
            )
            // このテーブルは一意聖を保証するためのテーブルなので、重複したら例外とする
            .execute()
    }

    @EventHandler
    fun on(event: UserDeletedEvent) {
        dsl.deleteFrom(LOOKUP_USER).where(LOOKUP_USER.ID.eq(event.id)).execute()
    }
}
