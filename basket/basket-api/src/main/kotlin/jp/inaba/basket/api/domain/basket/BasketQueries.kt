package jp.inaba.basket.api.domain.basket

import jp.inaba.common.domain.shared.Page
import jp.inaba.common.domain.shared.PagingCondition

data class FindBasketByIdQuery(
    val basketId: BasketId,
    val pagingCondition: PagingCondition,
)

data class FindBasketByIdResult(
    val page: Page<FindBasketByIdSummary>,
)

data class FindBasketByIdSummary(
    val itemId: String,
    val itemName: String,
    val itemPrice: Int,
    val itemPictureUrl: String,
    val itemQuantity: Int,
)
