package jp.inaba.service.application.command.basket

import jp.inaba.core.domain.basket.CreateBasketError
import jp.inaba.core.domain.common.UseCaseException
import jp.inaba.message.basket.command.CreateBasketCommand
import jp.inaba.service.domain.basket.CanCreateBasketVerifier
import jp.inaba.service.domain.basket.InternalCreateBasketCommand
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Component

@Component
class CreateBasketInteractor(
    private val canCreateBasketVerifier: CanCreateBasketVerifier,
    private val commandGateway: CommandGateway,
) {
    @CommandHandler
    fun handle(command: CreateBasketCommand) {
        if (canCreateBasketVerifier.isBasketExits(command.id)) {
            throw UseCaseException(CreateBasketError.BASKET_ALREADY_EXISTS)
        }
        if (canCreateBasketVerifier.isUserNotFound(command.userId)) {
            throw UseCaseException(CreateBasketError.USER_NOT_FOUND)
        }
        if (canCreateBasketVerifier.isLinkedToUser(command.userId)) {
            throw UseCaseException(CreateBasketError.BASKET_ALREADY_LINKED_TO_USER)
        }

        val internalCommand =
            InternalCreateBasketCommand(
                id = command.id,
                userId = command.userId,
            )

        commandGateway.sendAndWait<Any>(internalCommand)
    }
}
