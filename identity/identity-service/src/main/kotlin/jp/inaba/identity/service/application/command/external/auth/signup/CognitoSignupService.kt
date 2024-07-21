package jp.inaba.identity.service.application.command.external.auth.signup

interface CognitoSignupService {
    fun handle(emailAddress: String, password: String)
}
