package jp.inaba.basket.service.presentation.basket.setbasketitem

import jp.inaba.basket.api.domain.basket.*
import jp.inaba.basket.service.presentation.basket.BasketControllerBase
import jp.inaba.catalog.api.domain.product.ProductId
import jp.inaba.common.presentation.shared.ErrorResponse
import jp.inaba.identity.api.domain.user.UserId
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class SetBasketItemController(
    private val commandGateway: CommandGateway
) : BasketControllerBase() {
    @PostMapping("/{userId}/items")
    fun setBasketItem(
        @PathVariable("userId")
        rawUserId: String,
        @RequestBody
        request: SetBasketItemRequest
    ): ResponseEntity<Any> {
        val userId = UserId(rawUserId)
        val basketId = BasketId(userId)
        val productId = ProductId(request.productId)
        val basketItemQuantity = BasketItemQuantity(request.itemQuantity)
        val command = BasketCommands.SetBasketItem(
            id = basketId,
            productId = productId,
            basketItemQuantity = basketItemQuantity
        )

        val result = commandGateway.setBasketItem(command)

        return if (result.isOk) {
            ResponseEntity.ok().build()
        } else {
            when(result.error) {
                BasketErrors.SetBasketItem.PRODUCT_NOT_FOUND,
                BasketErrors.SetBasketItem.PRODUCT_MAX_KIND_OVER ->
                    ResponseEntity(
                        ErrorResponse(
                            errorCode = result.error.errorCode,
                            errorMessage = result.error.errorMessage
                        ),
                        HttpStatus.BAD_REQUEST
                    )
            }
        }
    }
}