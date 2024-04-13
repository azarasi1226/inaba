package jp.inaba.basket.service.presentation.basket.clear

import jp.inaba.basket.api.domain.basket.BasketCommands
import jp.inaba.basket.api.domain.basket.BasketId
import jp.inaba.basket.api.domain.basket.clearBasket
import jp.inaba.basket.service.presentation.basket.BasketControllerBase
import jp.inaba.identity.api.domain.user.UserId
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ClearBasketController(
    private val commandGateway: CommandGateway
) : BasketControllerBase() {
    @DeleteMapping("/{userId}/items")
    fun handle(
        @PathVariable("userId")
        rawUserId: String,
    ) {
        val userId = UserId(rawUserId)
        val basketId = BasketId(userId)
        val command = BasketCommands.Clear(basketId)

        commandGateway.clearBasket(command)
    }
}