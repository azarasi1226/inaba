package jp.inaba.identity.api.domain.external.auth.command

data class DeleteAuthUserCommand(
    override val emailAddress: String,
) : AuthCommand