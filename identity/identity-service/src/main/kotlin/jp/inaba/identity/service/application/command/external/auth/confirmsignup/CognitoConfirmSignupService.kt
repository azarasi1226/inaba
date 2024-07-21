package jp.inaba.identity.service.application.command.external.auth.confirmsignup

interface CognitoConfirmSignupService {
    fun handle(emailAddress: String, confirmCode: String)
}
