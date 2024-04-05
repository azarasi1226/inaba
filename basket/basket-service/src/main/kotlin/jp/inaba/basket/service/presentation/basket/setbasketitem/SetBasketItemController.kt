package jp.inaba.basket.service.presentation.basket.setbasketitem

import jp.inaba.basket.api.domain.basket.BasketCommands
import jp.inaba.basket.api.domain.basket.BasketId
import jp.inaba.basket.api.domain.basket.BasketItemQuantity
import jp.inaba.basket.service.presentation.basket.BasketControllerBase
import jp.inaba.catalog.api.domain.product.ProductId
import jp.inaba.common.domain.shared.DomainException
import jp.inaba.identity.api.domain.user.UserId
import org.axonframework.commandhandling.gateway.CommandGateway
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
    ) {
        val userId = UserId(rawUserId)
        val basketId = BasketId(userId)
        val productId = ProductId(request.productId)
        val basketItemQuantity = BasketItemQuantity(request.itemQuantity)
        val command = BasketCommands.SetBasketItem(
            id = basketId,
            productId = productId,
            basketItemQuantity = basketItemQuantity
        )

        val result = commandGateway.sendAndWait<Any>(command)

        if(result is DomainException) {
            println("エラーだったよ")
        }
    }
}