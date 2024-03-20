package jp.inaba.basket.api.domain.basket

import jp.inaba.common.domain.shared.Page
import jp.inaba.common.domain.shared.PagingCondition

object BasketQueries {
    data class FindByUserIdQuery(
        //TODO(値オブジェクト)
        val userId: String,
        val pagingCondition: PagingCondition
    )

    data class FindByUserIdResult(
        val basketId: String,
        val page: Page<ItemDataModel>
    )

    data class ItemDataModel(
        val itemId: String,
        val itemName: String,
        val itemPrice: Int,
        val itemPictureUrl: String,
        val itemQuantity: Int,
    )
}

