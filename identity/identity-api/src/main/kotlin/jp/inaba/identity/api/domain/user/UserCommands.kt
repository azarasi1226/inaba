package jp.inaba.identity.api.domain.user

interface UserCommand {
    val id: UserId
}

object UserCommands {
    data class Create(
        override val id: UserId
    ) : UserCommand

    data class UpdateAddress(
        override val id: UserId
    ) : UserCommand

    data class UpdateCreditCardInformation(
        override val id: UserId
    ) : UserCommand
}