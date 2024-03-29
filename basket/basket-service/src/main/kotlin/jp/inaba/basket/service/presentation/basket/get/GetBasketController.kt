package jp.inaba.basket.service.presentation.basket.get

import jp.inaba.basket.api.domain.basket.BasketId
import jp.inaba.basket.api.domain.basket.BasketQueries
import jp.inaba.basket.service.presentation.basket.BasketControllerBase
import jp.inaba.common.domain.shared.PagingCondition
import jp.inaba.identity.api.domain.user.UserId
import org.axonframework.extensions.kotlin.query
import org.axonframework.queryhandling.QueryGateway
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class GetBasketController(
    private val queryGateway: QueryGateway
) : BasketControllerBase() {
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
        val userId = UserId(rawUserId)
        val basketId = BasketId.fromUserId(userId)
        val query = BasketQueries.FindByUserIdQuery(basketId, pagingCondition)

        val result = queryGateway.query<BasketQueries.FindByUserIdResult, BasketQueries.FindByUserIdQuery>(query)
            .get()

        return GetBasketResponse(page = result.page)
    }
}