package jp.inaba.basket.service.presentation.basket.deletebasketitem

import jp.inaba.basket.api.domain.basket.BasketCommands
import jp.inaba.basket.api.domain.basket.BasketId
import jp.inaba.basket.service.presentation.basket.BasketControllerBase
import jp.inaba.catalog.api.domain.product.ProductId
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class DeleteBasketItemController(
    private val commandGateway: CommandGateway
) : BasketControllerBase() {
    @DeleteMapping("/{basketId}/items/{itemId}")
    fun deleteBasketItem(
        @PathVariable("basketId")
        rawBasketId: String,
        @PathVariable("itemId")
        rawItemId: String
    ) {
        val basketId = BasketId(rawBasketId)
        val productId = ProductId(rawItemId)
        val command = BasketCommands.DeleteBasketItem(
            id = basketId,
            productId = productId
        )

        commandGateway.sendAndWait<Any>(command)
    }
}