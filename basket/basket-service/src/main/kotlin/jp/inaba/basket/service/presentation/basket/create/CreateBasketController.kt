package jp.inaba.basket.service.presentation.basket.create

import jp.inaba.basket.api.domain.basket.BasketCommands
import jp.inaba.basket.api.domain.basket.createBasket
import jp.inaba.basket.service.presentation.basket.BasketControllerBase
import jp.inaba.common.presentation.shared.ErrorResponse
import jp.inaba.identity.api.domain.user.UserId
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
    ): ResponseEntity<Any> {
        val userId = UserId(request.userId)
        val command = BasketCommands.Create(userId)

        val result = commandGateway.createBasket(command)

        return if (result.isOk) {
            ResponseEntity.ok().build()
        } else {
            ResponseEntity(
                ErrorResponse(
                    errorCode = result.error.errorCode,
                    errorMessage = result.error.errorMessage
                ),
                HttpStatus.BAD_REQUEST
            )
        }
    }
}