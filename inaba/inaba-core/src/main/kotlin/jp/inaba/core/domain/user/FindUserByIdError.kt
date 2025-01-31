package jp.inaba.core.domain.user

import jp.inaba.core.domain.common.UseCaseError

enum class FindUserByIdError(
    override val errorCode: String,
    override val errorMessage: String,
) : UseCaseError {
    USER_NOT_FOUND("1", "ユーザーが存在しません"),
}
