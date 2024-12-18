package jp.inaba.catalog.service.presentation.product.update

import jp.inaba.catalog.api.domain.product.command.UpdateProductCommand
import jp.inaba.catalog.api.domain.product.updateProduct
import jp.inaba.catalog.service.presentation.product.ProductController
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import jp.inaba.catalog.share.domain.product.ProductDescription
import jp.inaba.catalog.share.domain.product.ProductId
import jp.inaba.catalog.share.domain.product.ProductImageURL
import jp.inaba.catalog.share.domain.product.ProductName
import jp.inaba.catalog.share.domain.product.ProductPrice

@RestController
class UpdateProductController(
    private val commandGateway: CommandGateway,
) : ProductController {
    @PutMapping("/{productId}")
    fun handle(
        @PathVariable("productId")
        rawProductId: String,
        @RequestBody
        request: UpdateProductRequest,
    ) {
        val productId = ProductId(rawProductId)
        val productName = ProductName(request.name)
        val productDescription = ProductDescription(request.description)
        val productImageUrl = ProductImageURL(request.imageUrl)
        val productPrice = ProductPrice(request.price)
        val command =
            UpdateProductCommand(
                id = productId,
                name = productName,
                description = productDescription,
                imageUrl = productImageUrl,
                price = productPrice,
            )

        commandGateway.updateProduct(command)
    }
}
