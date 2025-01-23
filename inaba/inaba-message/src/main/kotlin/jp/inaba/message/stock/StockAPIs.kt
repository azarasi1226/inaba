package jp.inaba.message.stock

import jp.inaba.message.stock.command.CreateStockCommand
import jp.inaba.message.stock.command.DecreaseStockCommand
import jp.inaba.message.stock.command.IncreaseStockCommand
import org.axonframework.commandhandling.gateway.CommandGateway

fun CommandGateway.createStock(command: CreateStockCommand) {
    this.sendAndWait<Any>(command)
}

fun CommandGateway.increaseStock(command: IncreaseStockCommand) {
    this.sendAndWait<Any>(command)
}

fun CommandGateway.decreaseStock(command: DecreaseStockCommand) {
    this.sendAndWait<Any>(command)
}
