package jp.inaba.service.application.command.basket

import jp.inaba.core.domain.basket.CreateBasketError
import jp.inaba.core.domain.common.CommonError
import jp.inaba.core.domain.common.UseCaseException
import jp.inaba.message.basket.command.CreateBasketCommand
import jp.inaba.service.domain.UniqueAggregateIdVerifier
import jp.inaba.service.domain.basket.CreateBasketVerifier
import jp.inaba.service.domain.basket.InternalCreateBasketCommand
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Component

@Component
class CreateBasketInteractor(
    private val uniqueAggregateIdVerifier: UniqueAggregateIdVerifier,
    private val createBasketVerifier: CreateBasketVerifier,
    private val commandGateway: CommandGateway,
) {
    @CommandHandler
    fun handle(command: CreateBasketCommand) {
        if (uniqueAggregateIdVerifier.hasDuplicateAggregateId(command.id.value)) {
            throw UseCaseException(CommonError.AGGREGATE_DUPLICATED)
        }
        if (createBasketVerifier.isUserNotFound(command.userId)) {
            throw UseCaseException(CreateBasketError.USER_NOT_FOUND)
        }
        if (createBasketVerifier.isLinkedToUser(command.userId)) {
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
