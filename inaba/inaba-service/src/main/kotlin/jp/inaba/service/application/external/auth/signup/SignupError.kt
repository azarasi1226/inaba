package jp.inaba.service.application.external.auth.signup

import jp.inaba.core.domain.common.UseCaseError

enum class SignupError(
    override val errorCode: String,
    override val errorMessage: String,
) : UseCaseError {
    UserNameExists("1", "すでに同じメールアドレスで登録済みです。"),
}
