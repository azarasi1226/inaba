package jp.inaba.service.application.saga.registerproduct

import io.github.oshai.kotlinlogging.KotlinLogging
import jp.inaba.message.stock.command.CreateStockCommand
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
            commandGateway.sendAndWait<Any>(command)
        } catch (e: Exception) {
            logger.error { "Stockの作成に失敗しました exception:[${e}]" }
            onFail.invoke()
        }
    }
}
