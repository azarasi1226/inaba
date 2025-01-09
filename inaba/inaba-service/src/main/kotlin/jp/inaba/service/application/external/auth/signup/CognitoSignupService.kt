package jp.inaba.service.application.external.auth.signup

interface CognitoSignupService {
    fun handle(
        emailAddress: String,
        password: String,
    )
}
