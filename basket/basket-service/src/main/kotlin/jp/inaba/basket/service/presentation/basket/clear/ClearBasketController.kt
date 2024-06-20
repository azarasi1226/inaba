package jp.inaba.basket.service.presentation.basket.clear

import jp.inaba.basket.api.domain.basket.BasketId
import jp.inaba.basket.api.domain.basket.ClearBasketCommand
import jp.inaba.basket.api.domain.basket.clearBasket
import jp.inaba.basket.service.presentation.basket.BasketController
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ClearBasketController(
    private val commandGateway: CommandGateway,
) : BasketController {
    @DeleteMapping("/{basketId}/products")
    fun handle(
        @PathVariable("basketId")
        rawBasketId: String,
    ) {
        val basketId = BasketId(rawBasketId)
        val command = ClearBasketCommand(basketId)

        commandGateway.clearBasket(command)
    }
}
