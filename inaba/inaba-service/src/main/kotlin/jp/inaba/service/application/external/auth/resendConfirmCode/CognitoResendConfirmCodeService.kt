package jp.inaba.service.application.external.auth.resendConfirmCode

interface CognitoResendConfirmCodeService {
    fun handle(emailAddress: String)
}
