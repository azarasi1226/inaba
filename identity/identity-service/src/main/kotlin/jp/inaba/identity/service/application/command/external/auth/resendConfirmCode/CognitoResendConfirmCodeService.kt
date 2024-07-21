package jp.inaba.identity.service.application.command.external.auth.resendConfirmCode

interface CognitoResendConfirmCodeService {
    fun handle(emailAddress: String)
}
