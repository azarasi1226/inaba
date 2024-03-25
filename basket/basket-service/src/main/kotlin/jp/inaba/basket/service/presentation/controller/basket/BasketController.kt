package jp.inaba.basket.service.presentation.controller.basket

import jp.inaba.basket.api.domain.basket.*
import jp.inaba.basket.service.application.command.basket.create.CreateBasketInputData
import jp.inaba.basket.service.application.command.basket.create.CreateBasketInteractor
import jp.inaba.basket.service.application.command.basket.setbasketitem.SetBasketItemInputData
import jp.inaba.basket.service.application.command.basket.setbasketitem.SetBasketItemInteractor
import jp.inaba.basket.service.presentation.model.basket.CreateBasketRequest
import jp.inaba.basket.service.presentation.model.basket.CreateBasketResponse
import jp.inaba.basket.service.presentation.model.basket.GetBasketResponse
import jp.inaba.basket.service.presentation.model.basket.SetBasketItemRequest
import jp.inaba.catalog.api.domain.product.ProductId
import jp.inaba.common.domain.shared.PagingCondition
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.extensions.kotlin.query
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
        val command = BasketCommands.Create(
            id = basketId,
            userId = request.userId
        )

        commandGateway.sendAndWait<Any>(command)

        return CreateBasketResponse(basketId.value)
    }

    @PostMapping("/{basketId}/items")
    fun setBasketItem(
        @PathVariable("basketId")
        rawBasketId: String,
        @RequestBody
        request: SetBasketItemRequest
    ) {
        val basketId = BasketId(rawBasketId)
        val productId = ProductId(request.productId)
        val basketItemQuantity = BasketItemQuantity(request.itemQuantity)
        val command = BasketCommands.SetBasketItem(
            id = basketId,
            productId = productId,
            basketItemQuantity = basketItemQuantity
        )

        commandGateway.sendAndWait<Any>(command)
    }

    @DeleteMapping("/{basketId}/items/{itemId}")
    fun deleteBasketItem(
        @PathVariable("basketId")
        rawBasketId: String,
        @PathVariable("itemId")
        rawItemId: String
    ) {
        val basketId = BasketId(rawBasketId)
        val productId = ProductId(rawItemId)
        val command = BasketCommands.DeleteBasketItem(
            id = basketId,
            productId = productId
        )

        commandGateway.sendAndWait<Any>(command)
    }

    @DeleteMapping("/{basketId}/items")
    fun clear(
        @PathVariable("basketId")
        rawBasketId: String,
    ) {
        val basketId = BasketId(rawBasketId)
        val command = BasketCommands.Clear(basketId)

        commandGateway.sendAndWait<Any>(command)
    }

    @GetMapping("/{userId}")
    fun getBasket(
        @PathVariable("userId")
        rawUserId: String,
        @RequestParam("pageSize")
        pageSize: Int,
        @RequestParam("pageNumber")
        pageNumber: Int
    ): GetBasketResponse {
        val pagingCondition = PagingCondition(
            pageSize = pageSize,
            pageNumber = pageNumber
        )
        val query = BasketQueries.FindByUserIdQuery(rawUserId, pagingCondition)

        val result = queryGateway.query<BasketQueries.FindByUserIdResult, BasketQueries.FindByUserIdQuery>(query)
            .get()

        return GetBasketResponse(
            basketId = result.basketId,
            page = result.page
        )
    }
}