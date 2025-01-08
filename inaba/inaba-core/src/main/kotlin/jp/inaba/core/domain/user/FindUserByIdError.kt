package jp.inaba.core.domain.user

import jp.inaba.core.domain.common.DomainError

// TODO(もっとちゃんerrorCode考えろ)
enum class FindUserByIdError(
    override val errorCode: String,
    override val errorMessage: String,
) : DomainError {
    USER_NOT_FOUND("00000", "ユーザーが存在しませんでした"),
}
