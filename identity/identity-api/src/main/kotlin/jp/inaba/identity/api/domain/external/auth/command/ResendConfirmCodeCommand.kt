package jp.inaba.identity.api.domain.external.auth.command

data class ResendConfirmCodeCommand(
    override val emailAddress: String,
) : AuthCommand