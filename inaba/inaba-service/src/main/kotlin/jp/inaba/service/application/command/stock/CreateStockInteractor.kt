package jp.inaba.service.application.command.stock

import jp.inaba.core.domain.common.CommonError
import jp.inaba.core.domain.common.UseCaseException
import jp.inaba.core.domain.stock.CreateStockError
import jp.inaba.message.stock.command.CreateStockCommand
import jp.inaba.service.domain.UniqueAggregateIdVerifier
import jp.inaba.service.domain.stock.CreateStockVerifier
import jp.inaba.service.domain.stock.InternalCreateStockCommand
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Component

@Component
class CreateStockInteractor(
    private val uniqueAggregateIdVerifier: UniqueAggregateIdVerifier,
    private val createStockVerifier: CreateStockVerifier,
    private val commandGateway: CommandGateway,
) {
    @CommandHandler
    fun handle(command: CreateStockCommand) {
        if (uniqueAggregateIdVerifier.hasDuplicateAggregateId(command.id.value)) {
            throw UseCaseException(CommonError.AGGREGATE_DUPLICATED)
        }
        if (createStockVerifier.isProductNotFound(command.productId)) {
            throw UseCaseException(CreateStockError.PRODUCT_NOT_FOUND)
        }
        if (createStockVerifier.isLinkedProduct(command.productId)) {
            throw UseCaseException(CreateStockError.STOCK_ALREADY_LINKED_TO_PRODUCT)
        }

        val internalCommand =
            InternalCreateStockCommand(
                id = command.id,
                productId = command.productId,
            )

        commandGateway.sendAndWait<Any>(internalCommand)
    }
}
