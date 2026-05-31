package jp.inaba.service2.projector

import jp.inaba.message.user.event.UserCreatedEvent
import jp.inaba.message.user.event.UserDeletedEvent
import jp.inaba.service.infrastructure.jooq.generated.tables.references.USERS
import jp.inaba.service2.utlis.toTokyoLocalDateTime
import org.axonframework.messaging.eventhandling.annotation.EventHandler
import org.axonframework.messaging.eventhandling.annotation.Timestamp
import org.axonframework.messaging.eventhandling.replay.annotation.ResetHandler
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class UserProjector(
    private val dsl: DSLContext,
) {
    @ResetHandler
    fun reset() {
        dsl.deleteFrom(USERS).execute()
    }

    @EventHandler
    fun on(
        event: UserCreatedEvent,
        @Timestamp timestamp: Instant,
    ) {
        dsl
            .insertInto(
                USERS,
                USERS.ID,
                USERS.NAME,
                USERS.CREATED_AT,
                USERS.UPDATED_AT,
            ).values(
                event.id,
                "ユーザー",
                timestamp.toTokyoLocalDateTime(),
                timestamp.toTokyoLocalDateTime(),
            )
            // 冪等性の考慮
            .onDuplicateKeyIgnore()
            .execute()
    }

    @EventHandler
    fun on(event: UserDeletedEvent) {
        dsl.deleteFrom(USERS).where(USERS.ID.eq(event.id)).execute()
    }
}
