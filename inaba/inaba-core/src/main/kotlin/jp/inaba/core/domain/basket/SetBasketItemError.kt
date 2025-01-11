package jp.inaba.core.domain.basket

import jp.inaba.core.domain.common.DomainError

enum class SetBasketItemError(
    override val errorCode: String,
    override val errorMessage: String,
) : DomainError {
    BASKET_DELETED("1", "削除済みの買い物かごです"),
    PRODUCT_NOT_FOUND("2", "商品が見つかりませんでした"),
    PRODUCT_MAX_KIND_OVER("3", "商品種類の上限数に到達しました"),
}
