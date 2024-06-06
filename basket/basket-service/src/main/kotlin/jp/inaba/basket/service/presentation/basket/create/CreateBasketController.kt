package jp.inaba.basket.service.presentation.basket.create

import com.github.michaelbull.result.mapBoth
import jp.inaba.basket.api.domain.basket.CreateBasketCommand
import jp.inaba.basket.api.domain.basket.CreateBasketError
import jp.inaba.basket.api.domain.basket.createBasket
import jp.inaba.basket.service.presentation.basket.BasketController
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
    private val commandGateway: CommandGateway,
) : BasketController {
    @PostMapping
    fun handle(
        @RequestBody
        request: CreateBasketRequest,
    ): ResponseEntity<Any> {
        val userId = UserId(request.userId)
        val command = CreateBasketCommand(userId)

        val result = commandGateway.createBasket(command)

        return result.mapBoth(
            success = {
                ResponseEntity
                    .status(HttpStatus.OK)
                    .body(it)
            },
            failure = {
                when (it) {
                    CreateBasketError.USER_NOT_FOUND ->
                        ResponseEntity
                            .status(HttpStatus.NOT_FOUND)
                            .body(ErrorResponse(it))
                }
            },
        )
    }
}
