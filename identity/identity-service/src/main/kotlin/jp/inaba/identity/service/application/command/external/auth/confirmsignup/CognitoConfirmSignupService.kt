package jp.inaba.identity.service.application.command.external.auth.confirmsignup

import jp.inaba.identity.api.domain.external.auth.AuthCommands

interface CognitoConfirmSignupService {
    fun handle(command: AuthCommands.ConfirmSignup)
}