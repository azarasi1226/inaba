package jp.inaba.core.domain.basket

import jp.inaba.core.domain.common.UseCaseError

enum class SetBasketItemError(
    override val errorCode: String,
    override val errorMessage: String,
) : UseCaseError {
    PRODUCT_NOT_FOUND("1", "商品が存在しませんでした"),
    OUT_OF_STOCK("2", "在庫を確保できません"),
    PRODUCT_MAX_KIND_OVER("3", "商品種類の上限数に到達しました"),
}
