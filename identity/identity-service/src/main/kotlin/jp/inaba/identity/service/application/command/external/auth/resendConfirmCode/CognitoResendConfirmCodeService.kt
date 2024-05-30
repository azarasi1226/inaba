package jp.inaba.identity.service.application.command.external.auth.resendConfirmCode

import jp.inaba.identity.api.domain.external.auth.AuthCommands

interface CognitoResendConfirmCodeService {
    fun handle(command: AuthCommands.ResendConfirmCode)
}
