package jp.inaba.message.user.command

import jp.inaba.core.domain.user.UserId
import jp.inaba.message.CommandResult
import jp.inaba.message.Error
import org.axonframework.messaging.commandhandling.gateway.CommandGateway
import org.axonframework.modelling.annotation.TargetEntityId

data class CreateUserCommand(
    @get:TargetEntityId
    val id: UserId,
    val subject: String,
)

object CreateUserResult {
    fun success() = CommandResult.success()
    fun alreadyExists() = CommandResult.faile(Error("すでに登録されたユーザーです"))
    fun alreadyLinkedSubject() = CommandResult.faile(Error("すでにリンクされたsubjectです"))
}

fun CommandGateway.createUser(command: CreateUserCommand): CommandResult =
    this.sendAndWait(command, CommandResult::class.java)
