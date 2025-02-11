package jp.inaba.service.application.command.product

import jp.inaba.core.domain.common.UseCaseException
import jp.inaba.core.domain.product.CreateProductError
import jp.inaba.message.product.command.CreateProductCommand
import jp.inaba.service.domain.product.CanCreateProductVerifier
import jp.inaba.service.domain.product.InternalCreateProductCommand
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Component

@Component
class CreateProductInteractor(
    private val canCreateProductVerifier: CanCreateProductVerifier,
    private val commandGateway: CommandGateway,
) {
    @CommandHandler
    fun handle(command: CreateProductCommand) {
        if (canCreateProductVerifier.isProductExists(command.id)) {
            throw UseCaseException(CreateProductError.PRODUCT_ALREADY_EXISTS)
        }

        val internalCommand =
            InternalCreateProductCommand(
                id = command.id,
                name = command.name,
                description = command.description,
                imageUrl = command.imageUrl,
                price = command.price,
            )

        commandGateway.sendAndWait<Any>(internalCommand)
    }
}
