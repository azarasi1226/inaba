package jp.inaba.service.application.command.product

import jp.inaba.core.domain.common.UseCaseException
import jp.inaba.core.domain.product.CreateProductError
import jp.inaba.message.product.command.CreateProductCommand
import jp.inaba.service.domain.product.CreateProductVerifier
import jp.inaba.service.domain.product.InternalCreateProductCommand
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Component

@Component
class CreateProductInteractor(
    private val verifier: CreateProductVerifier,
    private val commandGateway: CommandGateway,
) {
    @CommandHandler
    fun handle(command: CreateProductCommand) {
        if (verifier.isProductExists(command.id)) {
            throw UseCaseException(CreateProductError.PRODUCT_ALREADY_EXISTS)
        }
        if (verifier.isBrandNotFound(command.brandId)) {
            throw UseCaseException(CreateProductError.BRAND_NOT_FOUND)
        }

        val internalCommand =
            InternalCreateProductCommand(
                id = command.id,
                brandId = command.brandId,
                name = command.name,
                description = command.description,
                imageUrl = command.imageUrl,
                price = command.price,
            )

        commandGateway.sendAndWait<Any>(internalCommand)
    }
}
