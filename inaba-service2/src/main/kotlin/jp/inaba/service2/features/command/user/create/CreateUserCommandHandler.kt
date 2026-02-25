package jp.inaba.service2.features.user.create

import jp.inaba.core.domain.user.UserId
import jp.inaba.message.InabaEventTag
import jp.inaba.message.user.command.CreateUserCommand
import jp.inaba.message.user.command.CreateUserResult
import jp.inaba.message.user.event.UserCreatedEvent
import org.axonframework.eventsourcing.annotation.EventSourcingHandler
import org.axonframework.eventsourcing.annotation.reflection.EntityCreator
import org.axonframework.extension.spring.stereotype.EventSourced
import org.axonframework.messaging.commandhandling.annotation.CommandHandler
import org.axonframework.messaging.eventhandling.gateway.EventAppender
import org.axonframework.modelling.annotation.InjectEntity
import org.springframework.stereotype.Component

@Component
class CreateUserCommandHandler {
    @CommandHandler
    fun handle(
        command: CreateUserCommand,
        @InjectEntity state: State,
        eventAppender: EventAppender,
        subjectLinkedChecker: SubjectLinkedChecker,
    ): CreateUserResult {
        if (state.created) {
            return CreateUserResult.userAlreadyExists()
        }
        if (subjectLinkedChecker.handle(command.subject)) {
            return CreateUserResult.alreadyLinkedSubject()
        }

        eventAppender.append(
            UserCreatedEvent(
                id = command.id.value,
                subject = command.subject,
            ),
        )

        return CreateUserResult.success()
    }

    @EventSourced(tagKey = InabaEventTag.USER_ID, idType = UserId::class)
    class State(
        var created: Boolean,
    ) {
        @EntityCreator
        constructor() : this(
            created = false,
        )

        @EventSourcingHandler
        fun evolve(event: UserCreatedEvent) {
            created = true
        }
    }
}
