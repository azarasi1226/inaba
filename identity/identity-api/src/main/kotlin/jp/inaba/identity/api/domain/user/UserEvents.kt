package jp.inaba.identity.api.domain.user

interface UserEvent {
    val id: String
}

object UserEvents {
    data class Created(
        override val id: String
    ) : UserEvent

    data class ProfileInfoUpdated(
        override val id: String
    ) : UserEvent

    data class AddressInfoUpdated(
        override val id: String
    ) : UserEvent

    data class PaymentInfoUpdated(
        override val id: String
    ) : UserEvent

    data class Deleted(
        override val id: String
    ) : UserEvent
}