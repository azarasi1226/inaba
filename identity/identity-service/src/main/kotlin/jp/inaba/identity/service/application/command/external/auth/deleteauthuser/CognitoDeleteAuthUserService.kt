package jp.inaba.identity.service.application.command.external.auth.deleteauthuser

interface CognitoDeleteAuthUserService {
    fun handle(emailAddress: String)
}
