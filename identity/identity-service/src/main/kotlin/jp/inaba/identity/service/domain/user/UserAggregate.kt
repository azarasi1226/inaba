package jp.inaba.identity.service.domain.user

import jp.inaba.identity.api.domain.user.UserCommands
import jp.inaba.identity.api.domain.user.UserEvents
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
    constructor(command: UserCommands.Create) : this() {
        val event =
            UserEvents.Created(
                id = command.id.value,
            )

        AggregateLifecycle.apply(event)
    }

    @CommandHandler
    fun handle(command: UserCommands.UpdateProfileInfo) {
        val event =
            UserEvents.ProfileInfoUpdated(
                id = command.id.value,
            )

        AggregateLifecycle.apply(event)
    }

    @CommandHandler
    fun handle(command: UserCommands.UpdateAddressInfo) {
        val event =
            UserEvents.AddressInfoUpdated(
                id = command.id.value,
            )

        AggregateLifecycle.apply(event)
    }

    @CommandHandler
    fun handle(command: UserCommands.UpdatePaymentInfo) {
        val event =
            UserEvents.PaymentInfoUpdated(
                id = command.id.value,
            )

        AggregateLifecycle.apply(event)
    }

    @CommandHandler
    fun handle(command: UserCommands.Delete) {
        val event =
            UserEvents.Deleted(
                id = command.id.value,
            )

        AggregateLifecycle.apply(event)
    }

    @EventSourcingHandler
    fun on(event: UserEvents.Created) {
        id = UserId(event.id)
    }

    @EventSourcingHandler
    fun on(event: UserEvents.ProfileInfoUpdated) {
        // TODO()
    }

    @EventSourcingHandler
    fun on(event: UserEvents.AddressInfoUpdated) {
        // TODO()
    }

    @EventSourcingHandler
    fun on(event: UserEvents.PaymentInfoUpdated) {
        // TODO()
    }

    @EventSourcingHandler
    fun on(event: UserEvents.Deleted) {
        isDeleted = true
    }
}
