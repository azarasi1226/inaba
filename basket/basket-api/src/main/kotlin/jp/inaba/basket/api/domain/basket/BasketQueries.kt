package jp.inaba.basket.api.domain.basket

import jp.inaba.common.domain.shared.Page
import jp.inaba.common.domain.shared.PagingCondition

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