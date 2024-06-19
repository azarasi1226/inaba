package jp.inaba.basket.api.domain.basket

import jp.inaba.common.domain.shared.DomainError

// TODO(もっとちゃんerrorCode考えろ)
// TODO(削除されてたらエラーいらなく根...?)
enum class CreateBasketError(
    override val errorCode: String,
    override val errorMessage: String,
) : DomainError {
    USER_NOT_FOUND("1", "ユーザーが存在しませんでした"),
    BASKET_ALREADY_EXISTS("2", "指定のユーザーIDですでにバスケットが登録されています"),
}

enum class SetBasketItemError(
    override val errorCode: String,
    override val errorMessage: String,
) : DomainError {
    BASKET_DELETED("1", "削除済みの買い物かごです"),
    PRODUCT_NOT_FOUND("2", "商品が見つかりませんでした"),
    PRODUCT_MAX_KIND_OVER("3", "商品種類の上限数に到達しました"),
}

enum class DeleteBasketItemError(
    override val errorCode: String,
    override val errorMessage: String,
) : DomainError {
    BASKET_DELETED("1", "削除済みの買い物かごです"),
}

enum class ClearBasketError(
    override val errorCode: String,
    override val errorMessage: String,
) : DomainError {
    BASKET_DELETED("2", "削除済みの買い物かごです"),
}

enum class DeleteBasketError(
    override val errorCode: String,
    override val errorMessage: String,
) : DomainError {
    BASKET_DELETED("3", "削除済みの買い物かごです"),
}

enum class FindBasketByIdError(
    override val errorCode: String,
    override val errorMessage: String,
) : DomainError {
    BASKET_NOT_FOUND("1", "買い物かごが見つかりません"),
}
