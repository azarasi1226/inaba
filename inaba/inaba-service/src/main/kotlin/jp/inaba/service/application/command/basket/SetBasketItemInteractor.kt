package jp.inaba.service.application.command.basket

import com.github.michaelbull.result.onFailure
import jp.inaba.core.domain.common.ActionCommandResult
import jp.inaba.service.domain.basket.CanSetBasketItemVerifier
import jp.inaba.service.domain.basket.InternalSetBasketItemCommand
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Component
import jp.inaba.message.basket.command.SetBasketItemCommand

@Component
class SetBasketItemInteractor(
    private val canSetBasketItemVerifier: CanSetBasketItemVerifier,
    private val commandGateway: CommandGateway,
) {
    @CommandHandler
    fun handle(command: SetBasketItemCommand): ActionCommandResult {
        canSetBasketItemVerifier.checkProductExits(command.productId)
            .onFailure { return ActionCommandResult.error(it.errorCode) }

        val internalCommand =
            InternalSetBasketItemCommand(
                id = command.id,
                productId = command.productId,
                basketItemQuantity = command.basketItemQuantity,
            )

        return commandGateway.sendAndWait(internalCommand)
    }
}
