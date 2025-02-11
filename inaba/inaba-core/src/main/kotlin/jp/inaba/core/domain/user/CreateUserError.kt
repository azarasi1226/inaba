package jp.inaba.core.domain.user

import jp.inaba.core.domain.common.UseCaseError

enum class CreateUserError(
    override val errorCode: String,
    override val errorMessage: String,
) : UseCaseError {
    USER_ALREADY_EXISTS("1", "同じIDでユーザーが存在しています"),
    USER_ALREADY_LINKED_TO_SUBJECT("2", "別のユーザーにすでにリンク済みのSubjectです"),
}
