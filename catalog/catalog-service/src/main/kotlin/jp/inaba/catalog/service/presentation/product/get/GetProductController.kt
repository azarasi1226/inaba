package jp.inaba.catalog.service.presentation.product.get

import com.github.michaelbull.result.mapBoth
import jp.inaba.catalog.api.domain.product.findProductById
import jp.inaba.catalog.api.domain.product.query.FindProductByIdQuery
import jp.inaba.catalog.service.presentation.product.ProductController
import jp.inaba.catalog.share.domain.product.FindProductByIdError
import jp.inaba.catalog.share.domain.product.ProductId
import org.axonframework.queryhandling.QueryGateway
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class GetProductController(
    private val queryGateway: QueryGateway,
) : ProductController {
    @GetMapping("/{productId}")
    fun handle(
        @PathVariable("productId")
        rawProductId: String,
    ): ResponseEntity<Any> {
        val productId = ProductId(rawProductId)
        val query = FindProductByIdQuery(productId)

        return queryGateway.findProductById(query)
            .mapBoth(
                success = {
                    ResponseEntity
                        .status(HttpStatus.OK)
                        .body(GetProductResponse.create(it))
                },
                failure = {
                    when (it) {
                        FindProductByIdError.PRODUCT_NOT_FOUND ->
                            ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(it)
                    }
                },
            )
    }
}
