package jp.inaba.message.auth.command

data class ResendConfirmCodeCommand(
    override val emailAddress: String,
) : AuthCommand
