package jp.inaba.message.basket.query

import jp.inaba.core.domain.basket.BasketId
import jp.inaba.core.domain.common.Page
import jp.inaba.core.domain.common.PagingCondition
import jp.inaba.message.Error
import jp.inaba.message.QueryResult2
import org.axonframework.messaging.queryhandling.gateway.QueryGateway

data class FindBasketByIdQuery(
    val basketId: BasketId,
    val pagingCondition: PagingCondition,
)

data class FindBasketByIdData(
    val page: Page<BasketItem>,
)

data class BasketItem(
    val productId: String,
    val productName: String,
    val productPrice: Int,
    val productImageUrl: String?,
    val basketItemQuantity: Int,
)

class FindBasketByIdResult private constructor(
    override val success: Boolean,
    override val data: FindBasketByIdData?,
    override val error: Error?,
) : QueryResult2<FindBasketByIdData> {
    companion object {
        fun success(data: FindBasketByIdData) = FindBasketByIdResult(true, data, null)

        fun notFound() = FindBasketByIdResult(false, null, Error("買い物かごが存在しませんでした"))
    }
}

fun QueryGateway.findBasketById(query: FindBasketByIdQuery): FindBasketByIdResult =
    this.query(query, FindBasketByIdResult::class.java).join()
