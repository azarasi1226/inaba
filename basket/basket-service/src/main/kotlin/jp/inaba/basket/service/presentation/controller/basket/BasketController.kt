package jp.inaba.basket.service.presentation.controller.basket

import jp.inaba.basket.api.domain.basket.*
import jp.inaba.basket.service.application.command.basket.setproduct.SetBasketItemInputData
import jp.inaba.basket.service.application.command.basket.setproduct.SetBasketItemInteractor
import jp.inaba.basket.service.presentation.model.basket.CreateBasketRequest
import jp.inaba.basket.service.presentation.model.basket.CreateBasketResponse
import jp.inaba.basket.service.presentation.model.basket.SetBasketItemRequest
import jp.inaba.catalog.api.domain.product.ProductId
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.queryhandling.QueryGateway
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/baskets")
class BasketController(
    private val setBasketItemInteractor: SetBasketItemInteractor,
    private val commandGateway: CommandGateway,
    private val queryGateway: QueryGateway
) {
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

    @PostMapping("/{basketId}/items")
    fun create(
        @PathVariable("basketId")
        rawBasketId: String,
        @RequestBody
        request: SetBasketItemRequest
    ) {
        val basketId = BasketId(rawBasketId)
        val productId = ProductId(request.productId)
        val quantity = BasketItemQuantity(request.itemQuantity)
        val inputData = SetBasketItemInputData(
            basketId = basketId,
            productId = productId,
            basketItemQuantity = quantity
        )

        setBasketItemInteractor.handle(inputData)
    }

    @GetMapping("/{basketId}")
    fun get(
        @PathVariable("basketId")
        rawBasketId: String
    ) {

    }
}