package jp.inaba.service.application.command.basket

import jp.inaba.core.domain.basket.SetBasketItemError
import jp.inaba.core.domain.common.UseCaseException
import jp.inaba.message.basket.command.SetBasketItemCommand
import jp.inaba.service.domain.basket.InternalSetBasketItemCommand
import jp.inaba.service.domain.basket.SetBasketItemVerifier
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Component

@Component
class SetBasketItemInteractor(
    private val verifier: SetBasketItemVerifier,
    private val commandGateway: CommandGateway,
) {
    @CommandHandler
    fun handle(command: SetBasketItemCommand) {
        if (verifier.isProductNotFound(command.productId)) {
            throw UseCaseException(SetBasketItemError.PRODUCT_NOT_FOUND)
        }
        if (verifier.isOutOfStock(
                productId = command.productId,
                basketItemQuantity = command.basketItemQuantity,
            )
        ) {
            throw UseCaseException(SetBasketItemError.OUT_OF_STOCK)
        }

        val internalCommand =
            InternalSetBasketItemCommand(
                id = command.id,
                productId = command.productId,
                basketItemQuantity = command.basketItemQuantity,
            )

        commandGateway.sendAndWait<Any>(internalCommand)
    }
}
