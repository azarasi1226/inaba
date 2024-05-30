package jp.inaba.identity.service.application.command.external.auth.signup

import jp.inaba.identity.api.domain.external.auth.AuthCommands

interface CognitoSignupService {
    fun handle(command: AuthCommands.Signup)
}
