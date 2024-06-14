package jp.inaba.basket.service.application.command.basket

import com.github.michaelbull.result.onFailure
import jp.inaba.basket.api.domain.basket.BasketId
import jp.inaba.basket.api.domain.basket.CreateBasketCommand
import jp.inaba.basket.service.domain.basket.CanCreateBasketVerifier
import jp.inaba.basket.service.domain.basket.InternalCreateBasketCommand
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
    fun handle(command: CreateBasketCommand): ActionCommandResult {
        canCreateBasketVerifier.checkUserExits(command.userId)
            .onFailure { return ActionCommandResult.error(it.errorCode) }
        canCreateBasketVerifier.checkBasketExitsForUserid(command.userId)
            .onFailure { return ActionCommandResult.error(it.errorCode) }

        val internalCommand = InternalCreateBasketCommand(
            id = command.id,
            userId = command.userId,
        )

        commandGateway.sendAndWait<Any>(internalCommand)

        return ActionCommandResult.ok()
    }
}
