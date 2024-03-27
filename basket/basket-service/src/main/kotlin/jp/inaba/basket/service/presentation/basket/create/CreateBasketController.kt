package jp.inaba.basket.service.presentation.basket.create

import jp.inaba.basket.api.domain.basket.BasketCommands
import jp.inaba.basket.api.domain.basket.BasketId
import jp.inaba.basket.service.presentation.basket.BasketControllerBase
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.extensions.kotlin.send
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CreateBasketController(
    private val commandGateway: CommandGateway
) : BasketControllerBase() {
    @PostMapping
    fun create(
        @RequestBody
        request: CreateBasketRequest
    ): CreateBasketResponse {
        val basketId = BasketId()
        val command = BasketCommands.Create(
            id = basketId,
            userId = request.userId
        )

        commandGateway.sendAndWait<Any>(command)

        return CreateBasketResponse(basketId.value)
    }
}