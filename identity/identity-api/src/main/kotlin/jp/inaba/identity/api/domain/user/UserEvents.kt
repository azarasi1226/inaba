package jp.inaba.identity.api.domain.user

interface UserEvent {
    val id: String
}

object UserEvents {
    data class Created(
        override val id: String
    ) : UserEvent

    data class AddressUpdated(
        override val id: String
    ) : UserEvent

    data class CreditCardInformationUpdated(
        override val id: String
    ) : UserEvent
}