package jp.inaba.basket.service.presentation.basket.setbasketitem

import jp.inaba.basket.api.domain.basket.BasketCommands
import jp.inaba.basket.api.domain.basket.BasketId
import jp.inaba.basket.api.domain.basket.BasketItemQuantity
import jp.inaba.basket.service.presentation.basket.BasketControllerBase
import jp.inaba.catalog.api.domain.product.ProductId
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class SetBasketItemController(
    private val commandGateway: CommandGateway
) : BasketControllerBase() {
    @PostMapping("/{basketId}/items")
    fun setBasketItem(
        @PathVariable("basketId")
        rawBasketId: String,
        @RequestBody
        request: SetBasketItemRequest
    ) {
        val basketId = BasketId(rawBasketId)
        val productId = ProductId(request.productId)
        val basketItemQuantity = BasketItemQuantity(request.itemQuantity)
        val command = BasketCommands.SetBasketItem(
            id = basketId,
            productId = productId,
            basketItemQuantity = basketItemQuantity
        )

        commandGateway.sendAndWait<Any>(command)
    }
}