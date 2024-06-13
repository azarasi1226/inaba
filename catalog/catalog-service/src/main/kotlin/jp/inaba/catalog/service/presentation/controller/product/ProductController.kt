package jp.inaba.catalog.service.presentation.controller.product

import jp.inaba.catalog.api.domain.product.CreateProductCommand
import jp.inaba.catalog.api.domain.product.FindProductByIdQuery
import jp.inaba.catalog.api.domain.product.FindProductByIdResult
import jp.inaba.catalog.api.domain.product.ProductDescription
import jp.inaba.catalog.api.domain.product.ProductId
import jp.inaba.catalog.api.domain.product.ProductImageURL
import jp.inaba.catalog.api.domain.product.ProductName
import jp.inaba.catalog.api.domain.product.ProductPrice
import jp.inaba.catalog.api.domain.product.ProductQuantity
import jp.inaba.catalog.service.application.query.product.ProductNotFoundException
import jp.inaba.catalog.service.presentation.model.product.ProductCreateRequest
import jp.inaba.catalog.service.presentation.model.product.ProductCreateResponse
import jp.inaba.catalog.service.presentation.model.product.ProductFindByIdResponse
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.extensions.kotlin.queryOptional
import org.axonframework.queryhandling.QueryGateway
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/catalog/products")
class ProductController(
    private val commandGateway: CommandGateway,
    private val queryGateway: QueryGateway,
) {
    @PostMapping
    fun create(
        @RequestBody
        request: ProductCreateRequest,
    ): ProductCreateResponse {
        val productId = ProductId()
        val command =
            CreateProductCommand(
                id = productId,
                name = ProductName(request.name),
                description = ProductDescription(request.description),
                imageUrl = ProductImageURL(request.imageUrl),
                price = ProductPrice(request.price),
                quantity = ProductQuantity(request.quantity),
            )

        commandGateway.sendAndWait<Any>(command)

        return ProductCreateResponse(productId.value)
    }

    @GetMapping("/{id}")
    fun findById(
        @PathVariable("id")
        rawId: String,
    ): ProductFindByIdResponse {
        val productId = ProductId(rawId)
        val query = FindProductByIdQuery(productId)

        val result =
            queryGateway.queryOptional<FindProductByIdResult, FindProductByIdQuery>(query)
                .get()
                .orElseThrow { ProductNotFoundException(productId) }

        return ProductFindByIdResponse(
            name = result.name,
            description = result.description,
            imageUrl = result.imageUrl,
            price = result.price,
            quantity = result.quantity,
        )
    }
}
