package jp.inaba.core.domain.basket

import jp.inaba.core.domain.common.UseCaseError

enum class FindBasketByIdError(
    override val errorCode: String,
    override val errorMessage: String,
) : UseCaseError {
    BASKET_NOT_FOUND("1", "買い物かごが見つかりません"),
}
