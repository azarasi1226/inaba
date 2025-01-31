package jp.inaba.core.domain.basket

import jp.inaba.core.domain.common.UseCaseError

enum class CreateBasketError(
    override val errorCode: String,
    override val errorMessage: String,
) : UseCaseError {
    USER_NOT_FOUND("1", "ユーザーが存在しませんでした"),
    BASKET_ALREADY_EXISTS("2", "指定のユーザーIDですでにバスケットが登録されています"),
}
