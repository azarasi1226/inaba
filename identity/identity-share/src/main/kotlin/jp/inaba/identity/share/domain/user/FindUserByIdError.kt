package jp.inaba.identity.share.domain.user

import jp.inaba.common.domain.shared.DomainError

// TODO(もっとちゃんerrorCode考えろ)
enum class FindUserByIdError(
    override val errorCode: String,
    override val errorMessage: String,
) : DomainError {
    USER_NOT_FOUND("00000", "ユーザーが存在しませんでした"),
}
