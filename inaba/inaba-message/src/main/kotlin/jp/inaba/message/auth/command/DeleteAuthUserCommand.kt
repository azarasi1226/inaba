package jp.inaba.message.auth.command

data class DeleteAuthUserCommand(
    override val emailAddress: String,
) : AuthCommand
