package jp.inaba.basket.service.presentation.basket.create

import jp.inaba.basket.api.domain.basket.BasketCommands
import jp.inaba.basket.service.presentation.basket.BasketControllerBase
import jp.inaba.identity.api.domain.user.UserId
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CreateBasketController(
    private val commandGateway: CommandGateway
) : BasketControllerBase() {
    @PostMapping("")
    fun create(
        @RequestBody
        request: CreateBasketRequest
    ) {
        val userId = UserId(request.userId)
        val command = BasketCommands.Create(userId)

        commandGateway.sendAndWait<Any>(command)
    }
}