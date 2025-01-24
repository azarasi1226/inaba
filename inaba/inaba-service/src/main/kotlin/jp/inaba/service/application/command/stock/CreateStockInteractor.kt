package jp.inaba.service.application.command.stock

import com.github.michaelbull.result.onFailure
import jp.inaba.core.domain.common.ActionCommandResult
import jp.inaba.message.stock.command.CreateStockCommand
import jp.inaba.service.domain.stock.CanCreateStockVerifier
import jp.inaba.service.domain.stock.InternalCreateStockCommand
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Component

@Component
class CreateStockInteractor(
    private val canCreateStockVerifier: CanCreateStockVerifier,
    private val commandGateway: CommandGateway
) {
    @CommandHandler
    fun handle(command: CreateStockCommand): ActionCommandResult {
        canCreateStockVerifier.checkProductExits(command.productId)
            .onFailure { return ActionCommandResult.error(it.errorCode) }

        val internalCommand = InternalCreateStockCommand(
            id = command.id,
            productId = command.productId
        )

        commandGateway.sendAndWait<Any>(internalCommand)

        return ActionCommandResult.ok()
    }
}