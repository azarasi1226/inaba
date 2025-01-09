package jp.inaba.service.application.external.auth.confirmsignup

interface CognitoConfirmSignupService {
    fun handle(
        emailAddress: String,
        confirmCode: String,
    )
}
