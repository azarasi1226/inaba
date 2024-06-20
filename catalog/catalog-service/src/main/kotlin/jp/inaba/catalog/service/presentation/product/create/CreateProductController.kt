package jp.inaba.catalog.service.presentation.product.create

import jp.inaba.catalog.api.domain.product.CreateProductCommand
import jp.inaba.catalog.api.domain.product.ProductDescription
import jp.inaba.catalog.api.domain.product.ProductId
import jp.inaba.catalog.api.domain.product.ProductImageURL
import jp.inaba.catalog.api.domain.product.ProductName
import jp.inaba.catalog.api.domain.product.ProductPrice
import jp.inaba.catalog.api.domain.product.ProductQuantity
import jp.inaba.catalog.api.domain.product.createProduct
import jp.inaba.catalog.service.presentation.product.ProductController
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CreateProductController(
    private val commandGateway: CommandGateway
) : ProductController {
    @PostMapping
    fun handle(
        @RequestBody
        request: CreateProductRequest
    ) {
        val productId = ProductId()
        val productName = ProductName(request.name)
        val productDescription = ProductDescription(request.description)
        val productImageUrl = ProductImageURL(request.imageUrl)
        val productPrice = ProductPrice(request.price)
        val productQuantity = ProductQuantity(request.quantity)
        val command = CreateProductCommand(
            id = productId,
            name = productName,
            description = productDescription,
            imageUrl = productImageUrl,
            price = productPrice,
            quantity = productQuantity,
        )

        commandGateway.createProduct(command)
    }
}