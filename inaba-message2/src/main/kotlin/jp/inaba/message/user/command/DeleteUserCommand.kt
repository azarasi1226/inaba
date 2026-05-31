package jp.inaba.message.user.command

import jp.inaba.core.domain.user.UserId
import jp.inaba.message.CommandResult
import jp.inaba.message.Error
import org.axonframework.messaging.commandhandling.gateway.CommandGateway
import org.axonframework.modelling.annotation.TargetEntityId

data class DeleteUserCommand(
    @get:TargetEntityId
    val id: UserId,
)

object DeleteUserResult {
    fun success() = CommandResult.success()
    fun notFound() = CommandResult.faile(Error("存在しないユーザーです"))
}

fun CommandGateway.deleteUser(command: DeleteUserCommand): CommandResult =
    this.sendAndWait(command, CommandResult::class.java)