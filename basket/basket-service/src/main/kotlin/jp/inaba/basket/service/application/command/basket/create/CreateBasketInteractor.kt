package jp.inaba.basket.service.application.command.basket.create

import jp.inaba.basket.api.domain.basket.BasketCommands
import jp.inaba.basket.api.domain.basket.BasketId
import jp.inaba.basket.service.domain.basket.CanCreateBasketVerifier
import jp.inaba.basket.service.domain.basket.InternalBasketCommands
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Component

@Component
class CreateBasketInteractor(
    private val canCreateBasketVerifier: CanCreateBasketVerifier,
    private val commandGateway: CommandGateway
) {
    @CommandHandler
    fun handle(command: BasketCommands.Create) {
        if(!canCreateBasketVerifier.existUser(command.userId)) {
          throw UserNotFoundException(command.userId)
        }

        val basketId = BasketId(command.userId)
        val internalCommand = InternalBasketCommands.Create(basketId)

        commandGateway.sendAndWait<Any>(internalCommand)
    }
}