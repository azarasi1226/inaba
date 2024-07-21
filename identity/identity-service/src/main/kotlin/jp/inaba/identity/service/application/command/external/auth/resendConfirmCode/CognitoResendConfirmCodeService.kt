package jp.inaba.identity.service.application.command.external.auth.resendConfirmCode

import jp.inaba.identity.api.domain.external.auth.ResendConfirmCodeCommand

interface CognitoResendConfirmCodeService {
    fun handle(emailAddress: String)
}
