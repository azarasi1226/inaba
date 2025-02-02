package jp.inaba.service.application.saga.unregisterproduct

import io.github.oshai.kotlinlogging.KotlinLogging
import jp.inaba.message.stock.command.DeleteStockCommand
import org.axonframework.commandhandling.gateway.CommandGateway

private val logger = KotlinLogging.logger {}

class DeleteStockStep(
    private val commandGateway: CommandGateway,
) {
    fun handle(
        command: DeleteStockCommand,
        onFail: () -> Unit,
    ) {
        try {
            commandGateway.sendAndWait<Any>(command)
        } catch (e: Exception) {
            logger.error { "Stockの削除に失敗しました exception:[${e}]" }
            onFail.invoke()
        }
    }
}