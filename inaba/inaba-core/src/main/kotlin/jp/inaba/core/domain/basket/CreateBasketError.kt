package jp.inaba.core.domain.basket

import jp.inaba.core.domain.common.DomainError

enum class CreateBasketError(
    override val errorCode: String,
    override val errorMessage: String,
) : DomainError {
    USER_NOT_FOUND("1", "ユーザーが存在しませんでした"),
    BASKET_ALREADY_EXISTS("2", "指定のユーザーIDですでにバスケットが登録されています"),
}
