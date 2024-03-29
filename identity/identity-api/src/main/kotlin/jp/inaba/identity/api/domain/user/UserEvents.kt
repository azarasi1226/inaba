package jp.inaba.identity.api.domain.user

import org.axonframework.modelling.command.AggregateIdentifier

sealed interface UserEvent {
    val id: UserId
}
object UserEvents {
    data class Created(
        override val id: UserId
    ) : UserEvent

    data class AddressUpdated(
        override val id: UserId
    ) : UserEvent

    data class CreditCardInformationUpdated(
        override val id: UserId
    ) : UserEvent
}