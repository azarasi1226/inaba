package jp.inaba.basket.service.presentation.basket.deletebasketitem

import com.github.michaelbull.result.mapBoth
import jp.inaba.basket.api.domain.basket.BasketId
import jp.inaba.basket.api.domain.basket.DeleteBasketItemCommand
import jp.inaba.basket.api.domain.basket.deleteBasketItem
import jp.inaba.basket.service.presentation.basket.BasketController
import jp.inaba.catalog.api.domain.product.ProductId
import jp.inaba.common.presentation.shared.ErrorResponse
import jp.inaba.identity.api.domain.user.UserId
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
    @DeleteMapping("/{userId}/items/{itemId}")
    fun handle(
        @PathVariable("userId")
        rawUserId: String,
        @PathVariable("itemId")
        rawItemId: String,
    ): ResponseEntity<Any> {
        val userId = UserId(rawUserId)
        val basketId = BasketId(userId)
        val productId = ProductId(rawItemId)
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
                    ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(ErrorResponse(it))
                },
            )
    }
}
