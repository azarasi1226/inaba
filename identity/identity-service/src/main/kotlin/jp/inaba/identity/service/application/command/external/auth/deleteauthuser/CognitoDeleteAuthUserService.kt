package jp.inaba.identity.service.application.command.external.auth.deleteauthuser

import jp.inaba.identity.api.domain.external.auth.AuthCommands

interface CognitoDeleteAuthUserService {
    fun handle(command: AuthCommands.DeleteAuthUser)
}
