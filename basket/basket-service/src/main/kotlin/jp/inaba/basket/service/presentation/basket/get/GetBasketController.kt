package jp.inaba.basket.service.presentation.basket.get

import com.github.michaelbull.result.mapBoth
import jp.inaba.basket.api.domain.basket.findBasketById
import jp.inaba.basket.api.domain.basket.query.FindBasketByIdQuery
import jp.inaba.basket.service.presentation.basket.BasketController
import jp.inaba.basket.share.domain.basket.BasketId
import jp.inaba.basket.share.domain.basket.FindBasketByIdError
import jp.inaba.common.domain.shared.PagingCondition
import jp.inaba.common.presentation.shared.ErrorResponse
import org.axonframework.queryhandling.QueryGateway
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class GetBasketController(
    private val queryGateway: QueryGateway,
) : BasketController {
    @GetMapping("/{basketId}")
    fun handle(
        @PathVariable("basketId")
        rawBasketId: String,
        @RequestParam("pageSize")
        pageSize: Int,
        @RequestParam("pageNumber")
        pageNumber: Int,
    ): ResponseEntity<Any> {
        val pagingCondition =
            PagingCondition(
                pageSize = pageSize,
                pageNumber = pageNumber,
            )
        val basketId = BasketId(rawBasketId)
        val query = FindBasketByIdQuery(basketId, pagingCondition)

        return queryGateway.findBasketById(query)
            .mapBoth(
                success = {
                    ResponseEntity
                        .status(HttpStatus.OK)
                        .body(it)
                },
                failure = {
                    when (it) {
                        FindBasketByIdError.BASKET_NOT_FOUND ->
                            ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(ErrorResponse(it))
                    }
                },
            )
    }
}
