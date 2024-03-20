package jp.inaba.catalog.service.presentation.controller.product

import jp.inaba.catalog.api.domain.product.*
import jp.inaba.catalog.service.application.query.product.ProductNotFoundException
import jp.inaba.catalog.service.presentation.model.product.ProductCreateRequest
import jp.inaba.catalog.service.presentation.model.product.ProductCreateResponse
import jp.inaba.catalog.service.presentation.model.product.ProductFindByIdResponse
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.extensions.kotlin.queryOptional
import org.axonframework.queryhandling.QueryGateway
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/catalog/products")
class ProductController(
    private val commandGateway: CommandGateway,
    private val queryGateway: QueryGateway
) {
    @PostMapping
    fun create(
        @RequestBody
        request: ProductCreateRequest
    ): ProductCreateResponse {
        val productId = ProductId()
        val command  = ProductCommands.Create(
            id = productId,
            name = ProductName(request.name),
            description = request.description,
            imageUrl = request.imageUrl,
            price = ProductPrice(request.price),
            quantity = request.quantity
        )

        commandGateway.sendAndWait<Any>(command)

        return ProductCreateResponse(productId.value)
    }

    @GetMapping("/{id}")
    fun findById(
        @PathVariable("id")
        rawId: String
    ): ProductFindByIdResponse {
        val productId = ProductId(rawId)
        val query = ProductQueries.FindById(productId)

        val result = queryGateway.queryOptional<ProductQueries.FindByIdResult, ProductQueries.FindById>(query)
            .get()
            .orElseThrow { ProductNotFoundException(productId) }

        return ProductFindByIdResponse(
            name = result.name,
            description = result.description,
            imageUrl = result.imageUrl,
            price = result.price,
            quantity = result.quantity
        )
    }
}