package jp.inaba.message.basket.command

import jp.inaba.core.domain.user.UserId
import jp.inaba.message.CommandResult
import jp.inaba.message.Error
import org.axonframework.messaging.commandhandling.gateway.CommandGateway
import org.axonframework.modelling.annotation.TargetEntityId

data class ClearBasketCommand(
    @TargetEntityId
    val id: UserId,
)

object ClearBasketResult {
    fun success() = CommandResult.success()
    fun userNotFound() = CommandResult.faile(Error("ユーザーが存在しません"))
}

fun CommandGateway.clearBasket(command: ClearBasketCommand): CommandResult =
    this.sendAndWait(command, CommandResult::class.java)
