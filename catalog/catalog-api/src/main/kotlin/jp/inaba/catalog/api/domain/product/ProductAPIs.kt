package jp.inaba.catalog.api.domain.product

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import jp.inaba.catalog.api.domain.product.command.CreateProductCommand
import jp.inaba.catalog.api.domain.product.command.DeleteProductCommand
import jp.inaba.catalog.api.domain.product.command.UpdateProductCommand
import jp.inaba.catalog.api.domain.product.query.FindProductByIdQuery
import jp.inaba.catalog.api.domain.product.query.FindProductByIdResult
import jp.inaba.catalog.share.domain.product.FindProductByIdError
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.messaging.responsetypes.ResponseTypes
import org.axonframework.queryhandling.QueryGateway

fun CommandGateway.createProduct(command: CreateProductCommand) {
    this.sendAndWait<Any>(command)
}

fun CommandGateway.updateProduct(command: UpdateProductCommand) {
    this.sendAndWait<Any>(command)
}

fun CommandGateway.deleteProduct(command: DeleteProductCommand) {
    this.sendAndWait<Any>(command)
}

fun QueryGateway.findProductById(query: FindProductByIdQuery): Result<FindProductByIdResult, FindProductByIdError> {
    val maybeResult =
        this.query(query, ResponseTypes.optionalInstanceOf(FindProductByIdResult::class.java))
            .get()

    return if (maybeResult.isPresent) {
        Ok(maybeResult.get())
    } else {
        Err(FindProductByIdError.PRODUCT_NOT_FOUND)
    }
}
