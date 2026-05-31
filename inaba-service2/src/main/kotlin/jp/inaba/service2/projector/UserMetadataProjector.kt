package jp.inaba.service2.projector

import jp.inaba.message.user.event.UserCreatedEvent
import jp.inaba.service.infrastructure.jooq.generated.tables.references.USER_METADATA
import org.axonframework.messaging.eventhandling.annotation.EventHandler
import org.axonframework.messaging.eventhandling.replay.annotation.ResetHandler
import org.jooq.DSLContext
import org.springframework.stereotype.Component

@Component
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
                USER_METADATA.BASKET_ID,
            ).values(
                event.subject,
                event.id,
                event.id,
            )
            // 冪等性の考慮
            .onDuplicateKeyIgnore()
            .execute()
    }
}
