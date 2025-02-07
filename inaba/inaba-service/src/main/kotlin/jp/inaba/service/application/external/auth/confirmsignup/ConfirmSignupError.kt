package jp.inaba.service.application.external.auth.confirmsignup

import jp.inaba.core.domain.common.UseCaseError


enum class ConfirmSignupError(
    override val errorCode: String,
    override val errorMessage: String
): UseCaseError {
    ConfirmCodeMismatch("1", "確認コードが間違っています。"),
    ExpiredCode("2", "確認コードの期限がすぎています。"),
    NotAuthorized("3", "認可されていないメールアドレスです。"),
}