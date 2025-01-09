package jp.inaba.message.auth.command

data class SignupCommand(
    override val emailAddress: String,
    val password: String,
) : AuthCommand
