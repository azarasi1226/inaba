package jp.inaba.service2.features.command.user.create

import jp.inaba.message.user.event.UserCreatedEvent
import jp.inaba.service.infrastructure.jooq.generated.tables.references.LOOKUP_USERS
import org.axonframework.messaging.eventhandling.annotation.EventHandler
import org.jooq.DSLContext
import org.springframework.stereotype.Component

@Component
class SubjectUniqueValidator(
    private val dsl: DSLContext,
) {
    @EventHandler
    fun on(event: UserCreatedEvent) {
        dsl
            .insertInto(
                LOOKUP_USERS,
                LOOKUP_USERS.ID,
                LOOKUP_USERS.SUBJECT,
            ).values(
                event.id,
                event.subject,
            )
            // このテーブルはsubjectの一意性を保証するテーブルなので、重複したら例外
            .execute()
    }
}
