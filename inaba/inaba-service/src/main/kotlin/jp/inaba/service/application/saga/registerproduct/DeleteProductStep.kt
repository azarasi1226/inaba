package jp.inaba.service.application.saga.registerproduct

import io.github.oshai.kotlinlogging.KotlinLogging
import jp.inaba.message.product.command.DeleteProductCommand
import jp.inaba.message.product.deleteProduct
import org.axonframework.commandhandling.gateway.CommandGateway

private val logger = KotlinLogging.logger {}

class DeleteProductStep(
    private val commandGateway: CommandGateway,
) {
    fun handle(
        command: DeleteProductCommand,
        onFail: () -> Unit,
    ) {
        try {
            commandGateway.sendAndWait<Any>(command)
        } catch (e: Exception) {
            logger.warn { "Productの削除に失敗しました　exception:[$e]" }
            onFail.invoke()
        }
    }
}
