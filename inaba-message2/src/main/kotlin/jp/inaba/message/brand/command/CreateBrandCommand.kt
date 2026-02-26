package jp.inaba.message.brand.command

import jp.inaba.core.domain.brand.BrandId
import jp.inaba.core.domain.brand.BrandName
import jp.inaba.message.Error
import jp.inaba.message.CommandResult
import org.axonframework.messaging.commandhandling.gateway.CommandGateway
import org.axonframework.modelling.annotation.TargetEntityId

data class CreateBrandCommand(
    @TargetEntityId
    val id: BrandId,
    val name: BrandName,
)

object CreateBrandResult {
    fun success() = CommandResult.success()
    fun alreadyExists() = CommandResult.faile(Error("同じIDのブランドが既に存在しています"))
}

fun CommandGateway.createBrand(command: CreateBrandCommand): CommandResult =
    this.sendAndWait(command, CommandResult::class.java)