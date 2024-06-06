package jp.inaba.basket.api.domain.basket

import jp.inaba.common.domain.shared.DomainError

// TODO(もっとちゃんerrorCode考えろ)
enum class CreateBasketError(
    override val errorCode: String,
    override val errorMessage: String,
) : DomainError {
    USER_NOT_FOUND("B_B_C_001", "ユーザーが存在しませんでした"),
}

enum class SetBasketItemError(
    override val errorCode: String,
    override val errorMessage: String,
) : DomainError {
    PRODUCT_NOT_FOUND("B_B_S_001", "商品が見つかりませんでした"),
    PRODUCT_MAX_KIND_OVER("B_B_S_002", "商品種類の上限数に到達しました"),
}
