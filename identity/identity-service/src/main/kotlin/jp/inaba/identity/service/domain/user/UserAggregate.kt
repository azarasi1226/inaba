package jp.inaba.identity.service.domain.user

import jp.inaba.identity.api.domain.user.CreateUserCommand
import jp.inaba.identity.api.domain.user.DeleteUserCommand
import jp.inaba.identity.api.domain.user.UserCreatedEvent
import jp.inaba.identity.api.domain.user.UserDeletedEvent
import jp.inaba.identity.api.domain.user.UserId
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class UserAggregate() {
    @AggregateIdentifier
    private lateinit var id: UserId
    private var isDeleted: Boolean = false

    @CommandHandler
    constructor(command: CreateUserCommand) : this() {
        val event =
            UserCreatedEvent(
                id = command.id.value,
            )

        AggregateLifecycle.apply(event)
    }

    @CommandHandler
    fun handle(command: DeleteUserCommand) {
        val event =
            UserDeletedEvent(
                id = command.id.value,
            )

        AggregateLifecycle.apply(event)
    }

    @EventSourcingHandler
    fun on(event: UserCreatedEvent) {
        id = UserId(event.id)
    }

    @EventSourcingHandler
    fun on(event: UserDeletedEvent) {
        isDeleted = true
    }
}
