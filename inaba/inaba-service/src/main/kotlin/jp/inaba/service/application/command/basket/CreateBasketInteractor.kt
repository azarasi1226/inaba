package jp.inaba.service.application.command.basket

import com.github.michaelbull.result.onFailure
import jp.inaba.core.domain.common.ActionCommandResult
import jp.inaba.service.domain.basket.CanCreateBasketVerifier
import jp.inaba.service.domain.basket.InternalCreateBasketCommand
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Component
import jp.inaba.message.basket.command.CreateBasketCommand

@Component
class CreateBasketInteractor(
    private val canCreateBasketVerifier: CanCreateBasketVerifier,
    private val commandGateway: CommandGateway,
) {
    @CommandHandler
    fun handle(command: CreateBasketCommand): ActionCommandResult {
        canCreateBasketVerifier.checkUserExits(command.userId)
            .onFailure { return ActionCommandResult.error(it.errorCode) }
        canCreateBasketVerifier.checkUserHasNoBasket(command.userId)
            .onFailure { return ActionCommandResult.error(it.errorCode) }

        val internalCommand =
            InternalCreateBasketCommand(
                id = command.id,
                userId = command.userId,
            )

        commandGateway.sendAndWait<Any>(internalCommand)

        return ActionCommandResult.ok()
    }
}
