package jp.inaba.service2.feature.command.user.delete

import jp.inaba.core.domain.user.UserId
import jp.inaba.message.InabaEventTag
import jp.inaba.message.CommandResult
import jp.inaba.message.user.command.DeleteUserCommand
import jp.inaba.message.user.command.DeleteUserResult
import jp.inaba.message.user.event.UserCreatedEvent
import jp.inaba.message.user.event.UserDeletedEvent
import org.axonframework.eventsourcing.annotation.EventSourcingHandler
import org.axonframework.eventsourcing.annotation.reflection.EntityCreator
import org.axonframework.extension.spring.stereotype.EventSourced
import org.axonframework.messaging.commandhandling.annotation.CommandHandler
import org.axonframework.messaging.eventhandling.gateway.EventAppender
import org.axonframework.modelling.annotation.InjectEntity
import org.springframework.stereotype.Component

@Component
class DeleteUserCommandHandler {
    @CommandHandler
    fun handle(
        command: DeleteUserCommand,
        @InjectEntity state: State,
        eventAppender: EventAppender,
    ): CommandResult {
        if (!state.created) {
            return DeleteUserResult.notFound()
        }
        // 冪等性を考慮し、すでに削除されている場合は成功を返す
        if (state.deleted) {
            return DeleteUserResult.success()
        }

        eventAppender.append(
            UserDeletedEvent(
                id = command.id.value,
            ),
        )

        return DeleteUserResult.success()
    }

    @EventSourced(tagKey = InabaEventTag.USER_ID, idType = UserId::class)
    class State(
        var created: Boolean,
        var deleted: Boolean,
    ) {
        @EntityCreator
        constructor() : this(
            created = false,
            deleted = false,
        )

        @EventSourcingHandler
        fun evolve(event: UserCreatedEvent) {
            created = true
        }

        @EventSourcingHandler
        fun evolve(event: UserDeletedEvent) {
            deleted = true
        }
    }
}
