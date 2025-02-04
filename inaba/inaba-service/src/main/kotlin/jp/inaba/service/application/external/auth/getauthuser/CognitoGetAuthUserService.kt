package jp.inaba.service.application.external.auth.getauthuser

interface CognitoGetAuthUserService {
    fun handle(emailAddress: String): String
}