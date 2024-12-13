package jp.inaba.identity.api.domain.external.auth.command

data class SignupCommand(
    override val emailAddress: String,
    val password: String,
) : AuthCommand
