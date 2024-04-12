package jp.inaba.identity.api.domain.external.auth

import jp.inaba.identity.api.domain.user.UserId
import org.axonframework.commandhandling.RoutingKey

interface AuthCommand {
    @get:RoutingKey
    val emailAddress: String
}

object AuthCommands {
    data class Signup(
        override val emailAddress: String,
        val password: String
    ) : AuthCommand

    data class ConfirmSignup(
        override val emailAddress: String,
        val confirmCode: String
    ) : AuthCommand

    data class ResendConfirmCode(
        override val emailAddress: String
    ) : AuthCommand

    //TODO(これ汎用的なプロパティにするか、Command/Event名をもっと具体化したほうがよさそう)
    data class UpdateIdTokenAttribute(
        override val emailAddress: String,
        val userId: UserId
    ) : AuthCommand

    data class DeleteAuthUser(
        override val emailAddress: String
    ) : AuthCommand
}