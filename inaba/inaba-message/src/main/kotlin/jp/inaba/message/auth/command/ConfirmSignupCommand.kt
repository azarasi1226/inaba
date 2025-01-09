package jp.inaba.message.auth.command

data class ConfirmSignupCommand(
    override val emailAddress: String,
    val confirmCode: String,
) : AuthCommand
