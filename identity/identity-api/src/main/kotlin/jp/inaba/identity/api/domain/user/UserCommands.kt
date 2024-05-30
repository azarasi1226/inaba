package jp.inaba.identity.api.domain.user

interface UserCommand {
    val id: UserId
}

object UserCommands {
    data class Create(
        override val id: UserId,
    ) : UserCommand

    data class UpdateProfileInfo(
        override val id: UserId,
    ) : UserCommand

    data class UpdateAddressInfo(
        override val id: UserId,
    ) : UserCommand

    data class UpdatePaymentInfo(
        override val id: UserId,
    ) : UserCommand

    data class Delete(
        override val id: UserId,
    ) : UserCommand
}
