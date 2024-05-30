package jp.inaba.identity.service.application.command.external.auth.updateidtokenattribute

import jp.inaba.identity.api.domain.external.auth.AuthCommands

interface CognitoUpdateIdTokenAttributeForUserIdService {
    fun handle(command: AuthCommands.UpdateIdTokenAttributeForUserId)
}
