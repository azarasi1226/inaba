package jp.inaba.message.basket.query

import jp.inaba.core.domain.basket.BasketId
import jp.inaba.core.domain.common.Page
import jp.inaba.core.domain.common.PagingCondition

data class FindBasketByIdQuery(
    val basketId: BasketId,
    val pagingCondition: PagingCondition,
)

data class FindBasketByIdResult(
    val page: Page<BasketItem>,
) {
    data class BasketItem(
        val productId: String,
        val productName: String,
        val productPrice: Int,
        val productImageUrl: String,
        val productQuantity: Int,
    )
}
