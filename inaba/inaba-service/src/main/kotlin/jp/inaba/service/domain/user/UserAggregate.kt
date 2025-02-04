package jp.inaba.service.domain.user

import jp.inaba.core.domain.user.UserId
import jp.inaba.message.user.command.CreateUserCommand
import jp.inaba.message.user.command.DeleteUserCommand
import jp.inaba.message.user.event.UserCreatedEvent
import jp.inaba.message.user.event.UserDeletedEvent
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.modelling.command.AggregateMember
import org.axonframework.spring.stereotype.Aggregate

@Aggregate
class UserAggregate() {
    @AggregateIdentifier
    private lateinit var id: UserId

    @AggregateMember
    private var metadata = UserMetadata()

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
        AggregateLifecycle.markDeleted()
    }
}
