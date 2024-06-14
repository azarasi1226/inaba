package jp.inaba.basket.service.presentation.basket.setbasketitem

import com.github.michaelbull.result.mapBoth
import jp.inaba.basket.api.domain.basket.BasketId
import jp.inaba.basket.api.domain.basket.BasketItemQuantity
import jp.inaba.basket.api.domain.basket.SetBasketItemCommand
import jp.inaba.basket.api.domain.basket.SetBasketItemError
import jp.inaba.basket.api.domain.basket.setBasketItem
import jp.inaba.basket.service.presentation.basket.BasketController
import jp.inaba.catalog.api.domain.product.ProductId
import jp.inaba.common.presentation.shared.ErrorResponse
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class SetBasketItemController(
    private val commandGateway: CommandGateway,
) : BasketController {
    @PostMapping("/{basketId}/items")
    fun handle(
        @PathVariable("basketId")
        rawBasketId: String,
        @RequestBody
        request: SetBasketItemRequest,
    ): ResponseEntity<Any> {
        val basketId = BasketId(rawBasketId)
        val productId = ProductId(request.productId)
        val basketItemQuantity = BasketItemQuantity(request.itemQuantity)
        val command =
            SetBasketItemCommand(
                id = basketId,
                productId = productId,
                basketItemQuantity = basketItemQuantity,
            )

        val result = commandGateway.setBasketItem(command)

        return result.mapBoth(
            success = {
                ResponseEntity
                    .status(HttpStatus.OK)
                    .build()
            },
            failure = {
                when (it) {
                    SetBasketItemError.PRODUCT_NOT_FOUND ->
                        ResponseEntity
                            .status(HttpStatus.NOT_FOUND)
                            .body(ErrorResponse(it))
                    SetBasketItemError.PRODUCT_MAX_KIND_OVER ->
                        ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body(ErrorResponse(it))
                    SetBasketItemError.BASKET_DELETED ->
                        ResponseEntity
                            .status(HttpStatus.NOT_FOUND)
                            .body(ErrorResponse(it))
                }
            },
        )
    }
}
