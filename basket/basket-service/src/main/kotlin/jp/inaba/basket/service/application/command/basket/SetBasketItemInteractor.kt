package jp.inaba.basket.service.application.command.basket

import com.github.michaelbull.result.onFailure
import jp.inaba.basket.api.domain.basket.command.SetBasketItemCommand
import jp.inaba.basket.service.domain.basket.CanSetBasketItemVerifier
import jp.inaba.basket.service.domain.basket.InternalSetBasketItemCommand
import jp.inaba.common.domain.shared.ActionCommandResult
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Component

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
