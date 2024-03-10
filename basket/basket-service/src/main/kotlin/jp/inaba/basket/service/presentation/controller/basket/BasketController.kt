package jp.inaba.basket.service.presentation.controller.basket

import jp.ianba.basket.api.domain.basket.BasketId
import jp.ianba.basket.api.domain.basket.CreateBasketCommand
import jp.ianba.basket.api.domain.basket.ItemQuantity
import jp.ianba.basket.api.domain.basket.SetItemCommand
import jp.inaba.basket.service.presentation.model.basket.CreateBasketRequest
import jp.inaba.basket.service.presentation.model.basket.CreateBasketResponse
import jp.inaba.basket.service.presentation.model.basket.SetItemRequest
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.queryhandling.QueryGateway
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/baskets")
class BasketController(
    private val commandGateway: CommandGateway,
    private val queryGateway: QueryGateway
) {
    @PostMapping
    fun create(
        @RequestBody
        request: CreateBasketRequest
    ): CreateBasketResponse {
        val basketId = BasketId()
        val command = CreateBasketCommand(
            id = basketId,
            userId = request.userId
        )

        commandGateway.sendAndWait<Any>(command)

        return CreateBasketResponse(basketId.value)
    }

    @PostMapping("/{basketId}/items")
    fun create(
        @PathVariable("basketId")
        rawBasketId: String,
        @RequestBody
        request: SetItemRequest
    ) {
        val basketId = BasketId(rawBasketId)
        val itemQuantity = ItemQuantity(request.itemQuantity)
        val command = SetItemCommand(
            id = basketId,
            itemId = request.itemId,
            itemQuantity = itemQuantity
        )

        commandGateway.sendAndWait<Any>(command)
    }

    @GetMapping("/{basketId}")
    fun get(
        @PathVariable("basketId")
        rawBasketId: String
    ) {

    }
}