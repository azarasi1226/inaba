package jp.inaba.service.application.saga.registproduct

import io.github.oshai.kotlinlogging.KotlinLogging
import jp.inaba.message.stock.command.CreateStockCommand
import jp.inaba.message.stock.createStock
import org.axonframework.commandhandling.gateway.CommandGateway

private val logger = KotlinLogging.logger {}

class CreateStockStep(
    private val commandGateway: CommandGateway,
) {
    fun handle(
        command: CreateStockCommand,
        onFail: () -> Unit,
    ) {
        try {
            commandGateway.createStock(command)
        } catch (e: Exception) {
            logger.warn { "stockの作成に失敗しました exception:[$e]" }
            onFail.invoke()
        }
    }
}
