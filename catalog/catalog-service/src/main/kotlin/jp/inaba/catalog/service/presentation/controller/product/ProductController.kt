package jp.inaba.catalog.service.presentation.controller.product

import jp.inaba.catalog.api.domain.product.CreateProductCommand
import jp.inaba.catalog.api.domain.product.ProductId
import jp.inaba.catalog.service.presentation.model.product.CreateProductRequest
import jp.inaba.catalog.service.presentation.model.product.CreateProductResponse
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.queryhandling.QueryGateway
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/catalog/products")
class ProductController(
    private val commandGateway: CommandGateway,
    private val queryGateway: QueryGateway
) {
    @PostMapping
    fun create(
        @RequestBody
        request: CreateProductRequest
    ): CreateProductResponse {
        val productId = ProductId()
        val command  = CreateProductCommand(
            id = productId,
            productName = request.productName,
            description = request.description,
            imageUrl = request.imageUrl,
            price = request.price,
            quantity = request.quantity
        )

        commandGateway.sendAndWait<Any>(command)

        return CreateProductResponse(productId.value)
    }
}