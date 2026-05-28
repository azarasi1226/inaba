package jp.inaba.message.user.command

import jp.inaba.core.domain.user.UserId
import jp.inaba.message.CommandResult
import jp.inaba.message.Error
import org.axonframework.messaging.commandhandling.gateway.CommandGateway
import org.axonframework.modelling.annotation.TargetEntityId

data class CreateUserCommand(
    val oidcIssuer: String,
    val oidcSubject: String,
    val oidcIdentityProvider: String,
    val email: String,
    val emailVerified: Boolean
)

object CreateUserCommandResult {
    fun success() = CommandResult.success()
    fun emailNotVerified() = CommandResult.fail(Error("Emailが検証されていません"))
}

suspend fun CommandGateway.createUser(command: CreateUserCommand): CommandResult {
    return send(command, CommandResult::class.java).await()
}