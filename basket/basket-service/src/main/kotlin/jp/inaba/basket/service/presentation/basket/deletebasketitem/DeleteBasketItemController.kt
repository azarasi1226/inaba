package jp.inaba.basket.service.presentation.basket.deletebasketitem

import com.github.michaelbull.result.mapBoth
import jp.inaba.basket.api.domain.basket.BasketId
import jp.inaba.basket.api.domain.basket.DeleteBasketItemCommand
import jp.inaba.basket.api.domain.basket.DeleteBasketItemError
import jp.inaba.basket.api.domain.basket.deleteBasketItem
import jp.inaba.basket.service.presentation.basket.BasketController
import jp.inaba.catalog.api.domain.product.ProductId
import jp.inaba.common.presentation.shared.ErrorResponse
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class DeleteBasketItemController(
    private val commandGateway: CommandGateway,
) : BasketController {
    @DeleteMapping("/{basketId}/products/{productId}")
    fun handle(
        @PathVariable("basketId")
        rawBasketId: String,
        @PathVariable("productId")
        rawProductId: String,
    ): ResponseEntity<Any> {
        val basketId = BasketId(rawBasketId)
        val productId = ProductId(rawProductId)
        val command =
            DeleteBasketItemCommand(
                id = basketId,
                productId = productId,
            )

        return commandGateway.deleteBasketItem(command)
            .mapBoth(
                success = {
                    ResponseEntity
                        .status(HttpStatus.OK)
                        .build()
                },
                failure = {
                    when (it) {
                        DeleteBasketItemError.BASKET_DELETED ->
                            ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(ErrorResponse(it))
                    }
                },
            )
    }
}
