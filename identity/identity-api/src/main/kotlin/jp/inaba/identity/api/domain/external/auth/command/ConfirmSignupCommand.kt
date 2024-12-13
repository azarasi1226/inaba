package jp.inaba.identity.api.domain.external.auth.command

data class ConfirmSignupCommand(
    override val emailAddress: String,
    val confirmCode: String,
) : AuthCommand
