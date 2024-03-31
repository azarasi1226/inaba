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

    @CommandHandler
    constructor(command: UserCommands.Create): this() {
        val event = UserEvents.Created(
            id = command.id.value
        )

        AggregateLifecycle.apply(event)
    }

    @CommandHandler
    fun handle(command: UserCommands.UpdateAddress) {
        val event = UserEvents.AddressUpdated(
            id = command.id.value
        )

        AggregateLifecycle.apply(event)
    }

    @CommandHandler
    fun handle(command: UserCommands.UpdateCreditCardInformation) {
        val event = UserEvents.CreditCardInformationUpdated(
            id = command.id.value
        )

        AggregateLifecycle.apply(event)
    }

    @EventSourcingHandler
    fun on(event: UserEvents.Created) {
        id = UserId(event.id)
    }

    @EventSourcingHandler
    fun on(event: UserEvents.AddressUpdated) {
        //TODO()
    }

    @EventSourcingHandler
    fun on(event: UserEvents.CreditCardInformationUpdated) {
        //TODO()
    }
}