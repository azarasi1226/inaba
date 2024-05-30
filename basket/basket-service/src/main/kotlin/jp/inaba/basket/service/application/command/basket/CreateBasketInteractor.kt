package jp.inaba.basket.service.application.command.basket

import jp.inaba.basket.api.domain.basket.BasketCommands
import jp.inaba.basket.api.domain.basket.BasketErrors
import jp.inaba.basket.api.domain.basket.BasketId
import jp.inaba.basket.service.domain.basket.CanCreateBasketVerifier
import jp.inaba.basket.service.domain.basket.InternalBasketCommands
import jp.inaba.common.domain.shared.ActionCommandResult
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Component

@Component
class CreateBasketInteractor(
    private val canCreateBasketVerifier: CanCreateBasketVerifier,
    private val commandGateway: CommandGateway,
) {
    @CommandHandler
    fun handle(command: BasketCommands.Create): ActionCommandResult {
        if (!canCreateBasketVerifier.existUser(command.userId)) {
            return ActionCommandResult.error(BasketErrors.Create.USER_NOT_FOUND.errorCode)
        }

        val basketId = BasketId(command.userId)
        val internalCommand = InternalBasketCommands.Create(basketId)

        commandGateway.sendAndWait<Any>(internalCommand)

        return ActionCommandResult.ok()
    }
}
