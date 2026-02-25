package jp.inaba.message.product

import jp.inaba.message.product.command.CreateProductCommand
import jp.inaba.message.product.command.CreateProductResult
import org.axonframework.messaging.commandhandling.gateway.CommandGateway

fun CommandGateway.createProduct(command: CreateProductCommand): CreateProductResult {
    return this.sendAndWait(command, CreateProductResult::class.java)
}
