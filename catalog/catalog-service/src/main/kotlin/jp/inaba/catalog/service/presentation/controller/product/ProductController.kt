package jp.inaba.catalog.service.presentation.controller.product

import jp.inaba.catalog.api.domain.product.*
import jp.inaba.catalog.service.presentation.model.product.ProductCreateRequest
import jp.inaba.catalog.service.presentation.model.product.ProductCreateResponse
import jp.inaba.catalog.service.presentation.model.product.ProductFindByIdResponse
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.extensions.kotlin.query
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
            price = Price(request.price),
            quantity = request.quantity
        )

        commandGateway.sendAndWait<Any>(command)

        return ProductCreateResponse(productId.value)
    }

    @GetMapping("/{id}")
    fun findByProductId(
        @PathVariable("id")
        rawId: String
    ): ProductFindByIdResponse {
        val productId = ProductId(rawId)
        val query = ProductQueries.FindById(productId)

        val result = queryGateway.query<ProductQueries.FindByIdResult, ProductQueries.FindById>(query)
            .get()

        return ProductFindByIdResponse(
            name = result.name,
            description = result.description,
            imageUrl = result.imageUrl,
            price = result.price,
            quantity = result.quantity
        )
    }
}