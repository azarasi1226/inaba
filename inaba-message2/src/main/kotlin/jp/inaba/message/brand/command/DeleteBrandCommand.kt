package jp.inaba.message.brand.command

import jp.inaba.core.domain.brand.BrandId
import jp.inaba.message.CommandResult
import jp.inaba.message.Error
import org.axonframework.messaging.commandhandling.gateway.CommandGateway
import org.axonframework.modelling.annotation.TargetEntityId

data class DeleteBrandCommand(
    @TargetEntityId
    val id: BrandId,
)

object DeleteBrandResult {
    fun success(): CommandResult = CommandResult.success()
    fun notFound(): CommandResult = CommandResult.faile(Error("ブランドが存在しませんでした"))
}

fun CommandGateway.deleteBrand(command: DeleteBrandCommand): CommandResult =
    this.sendAndWait(command, CommandResult::class.java)
