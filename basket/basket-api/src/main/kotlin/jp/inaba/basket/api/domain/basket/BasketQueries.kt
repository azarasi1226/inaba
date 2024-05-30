package jp.inaba.basket.api.domain.basket

import jp.inaba.common.domain.shared.Page
import jp.inaba.common.domain.shared.PagingCondition

object BasketQueries {
    data class FindByIdQuery(
        val basketId: BasketId,
        val pagingCondition: PagingCondition,
    )

    data class FindByIdResult(
        val page: Page<BasketItemDataModel>,
    )

    data class BasketItemDataModel(
        val itemId: String,
        val itemName: String,
        val itemPrice: Int,
        val itemPictureUrl: String,
        val itemQuantity: Int,
    )
}
