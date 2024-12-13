package jp.inaba.basket.service.presentation.basket.deletebasketitem

import jp.inaba.basket.share.domain.basket.BasketId
import jp.inaba.basket.api.domain.basket.command.DeleteBasketItemCommand
import jp.inaba.basket.api.domain.basket.deleteBasketItem
import jp.inaba.basket.service.presentation.basket.BasketController
import jp.inaba.catalog.api.domain.product.ProductId
import org.axonframework.commandhandling.gateway.CommandGateway
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
    ) {
        val basketId = BasketId(rawBasketId)
        val productId = ProductId(rawProductId)
        val command =
            DeleteBasketItemCommand(
                id = basketId,
                productId = productId,
            )

        commandGateway.deleteBasketItem(command)
    }
}
