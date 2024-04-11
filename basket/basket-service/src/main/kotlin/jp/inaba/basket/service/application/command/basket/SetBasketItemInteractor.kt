package jp.inaba.basket.service.application.command.basket

import jp.inaba.basket.api.domain.basket.BasketCommandErrors
import jp.inaba.basket.api.domain.basket.BasketCommands
import jp.inaba.basket.service.domain.basket.CanSetBasketItemVerifier
import jp.inaba.basket.service.domain.basket.InternalBasketCommands
import jp.inaba.common.domain.shared.ActionCommandResult
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Component

@Component
class SetBasketItemInteractor(
    private val canSetBasketItemVerifier: CanSetBasketItemVerifier,
    private val commandGateway: CommandGateway
) {
    @CommandHandler
    fun handle(command: BasketCommands.SetBasketItem): ActionCommandResult {
        if(!canSetBasketItemVerifier.existProduct(command.productId)) {
            return ActionCommandResult.error(BasketCommandErrors.SetBasketItem.PRODUCT_NOT_FOUND.errorCode)
        }

        val internalCommand = InternalBasketCommands.SetBasketItem(
            id = command.id,
            productId = command.productId,
            basketItemQuantity = command.basketItemQuantity
        )

        return commandGateway.sendAndWait(internalCommand)
    }
}