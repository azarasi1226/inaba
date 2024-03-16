package jp.inaba.basket.api.domain.basket

import jp.inaba.common.domain.shared.Page
import jp.inaba.common.domain.shared.PagingCondition

data class BasketFindByUserIdQuery(
    //値オブジェクトにする
    val userId: String,
    val pagingCondition: PagingCondition
)

data class BasketFindByUserIdResult(
    val basketId: String,
    val userId: String,
    val page: Page<ItemDataModel>
)

data class ItemDataModel(
    val itemId: String,
    val itemName: String,
    val itemPrice: Int,
    val itemPictureUrl: String,
    val itemStockQuantity: Int,
    val itemQuantity: Int,
)