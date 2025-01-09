package jp.inaba.service.application.external.auth.deleteauthuser

interface CognitoDeleteAuthUserService {
    fun handle(emailAddress: String)
}
