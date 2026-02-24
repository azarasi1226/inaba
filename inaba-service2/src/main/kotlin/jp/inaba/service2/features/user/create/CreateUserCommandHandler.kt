package jp.inaba.service2.features.user.create

import com.fasterxml.jackson.annotation.JsonTypeInfo
import jp.inaba.core.domain.user.UserId
import org.axonframework.eventsourcing.annotation.EventSourcingHandler
import org.axonframework.eventsourcing.annotation.EventTag
import org.axonframework.eventsourcing.annotation.reflection.EntityCreator
import org.axonframework.extension.spring.stereotype.EventSourced
import org.axonframework.messaging.commandhandling.annotation.CommandHandler
import org.axonframework.messaging.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.eventhandling.gateway.EventAppender
import org.axonframework.modelling.annotation.InjectEntity
import org.axonframework.modelling.annotation.TargetEntityId
import org.springframework.stereotype.Component

data class CreateUserCommand(
    @TargetEntityId
    val id: UserId,
    val subject: String,
)

data class UserCreatedEvent(
    @EventTag("userId")
    val id: String,
    val subject: String,
)

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
sealed interface CreateUserResult {
    object Success : CreateUserResult

    data class UserAlreadyExists(
        val userId: String,
    ) : CreateUserResult

    data class AlreadyLinkedSubject(
        val subjectId: String,
    ) : CreateUserResult
}

interface CommandResult {
    val success: Boolean
    val errorMessage: String?
}

class CreateUserResult2 private constructor(
    override val success: Boolean,
    override val errorMessage: String?,
) : CommandResult {
    companion object {
        fun success(): CreateUserResult2 = CreateUserResult2(true, null)

        fun userAlreadyExists(): CreateUserResult2 = CreateUserResult2(false, "すでに登録されたユーザーです")

        fun alreadyLinkedSubject(): CreateUserResult2 = CreateUserResult2(false, "すでにリンクされたsubjectです")
    }
}

fun CommandGateway.createUser(command: CreateUserCommand): CreateUserResult2 = sendAndWait(command, CreateUserResult2::class.java)

class CommandException(
    val errorMessage: String,
) : Exception(errorMessage)

fun CommandResult.throwIfError() {
    if (!this.success) throw CommandException(this.errorMessage!!)
}

@Component
class CreateUserCommandHandler {
    @CommandHandler
    fun handle(
        command: CreateUserCommand,
        @InjectEntity state: State,
        eventAppender: EventAppender,
        subjectLinkedChecker: SubjectLinkedChecker,
    ): CreateUserResult2 {
        if (state.created) {
            return CreateUserResult2.userAlreadyExists()
        }

        if (subjectLinkedChecker.handle(command.subject)) {
            return CreateUserResult2.alreadyLinkedSubject()
        }

        eventAppender.append(
            UserCreatedEvent(
                id = command.id.value,
                subject = command.subject,
            ),
        )
        return CreateUserResult2.success()
    }

    @EventSourced(tagKey = "userId", idType = UserId::class)
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
