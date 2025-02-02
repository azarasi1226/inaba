package jp.inaba.service.application.saga.unregisterproduct

import io.github.oshai.kotlinlogging.KotlinLogging
import jp.inaba.message.product.command.DeleteProductCommand
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
            logger.error { "Productの削除に失敗しました exception:[${e}]" }
            onFail.invoke()
        }
    }
}