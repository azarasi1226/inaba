package jp.inaba.basket.service.presentation.basket.clear

import com.github.michaelbull.result.mapBoth
import jp.inaba.basket.api.domain.basket.BasketId
import jp.inaba.basket.api.domain.basket.ClearBasketCommand
import jp.inaba.basket.api.domain.basket.ClearBasketError
import jp.inaba.basket.api.domain.basket.clearBasket
import jp.inaba.basket.service.presentation.basket.BasketController
import jp.inaba.common.presentation.shared.ErrorResponse
import jp.inaba.identity.api.domain.user.UserId
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ClearBasketController(
    private val commandGateway: CommandGateway,
) : BasketController {
    @DeleteMapping("/{userId}/items")
    fun handle(
        @PathVariable("userId")
        rawUserId: String,
    ): ResponseEntity<Any> {
        val userId = UserId(rawUserId)
        val basketId = BasketId(userId)
        val command = ClearBasketCommand(basketId)

        return commandGateway.clearBasket(command)
            .mapBoth(
                success = {
                    ResponseEntity
                        .status(HttpStatus.OK)
                        .build()
                },
                failure = {
                    when (it) {
                        ClearBasketError.BASKET_DELETED ->
                            ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(ErrorResponse(it))
                    }
                },
            )
    }
}
