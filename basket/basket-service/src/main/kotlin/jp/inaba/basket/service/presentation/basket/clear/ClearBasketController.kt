package jp.inaba.basket.service.presentation.basket.clear

import jp.inaba.basket.api.domain.basket.BasketCommands
import jp.inaba.basket.api.domain.basket.BasketId
import jp.inaba.basket.service.presentation.basket.BasketControllerBase
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ClearBasketController(
    private val commandGateway: CommandGateway
) : BasketControllerBase() {
    @DeleteMapping("/{basketId}/items")
    fun clear(
        @PathVariable("basketId")
        rawBasketId: String,
    ) {
        val basketId = BasketId(rawBasketId)
        val command = BasketCommands.Clear(basketId)

        commandGateway.sendAndWait<Any>(command)
    }
}