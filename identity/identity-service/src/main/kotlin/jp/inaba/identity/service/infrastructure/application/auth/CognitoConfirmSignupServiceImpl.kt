package jp.inaba.identity.service.infrastructure.application.auth

import jp.inaba.identity.api.domain.external.auth.AuthCommands
import jp.inaba.identity.service.application.command.external.auth.confirmsignup.CognitoConfirmSignupService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmSignUpRequest

@Service
class CognitoConfirmSignupServiceImpl(
    @Value("\${aws.cognito.client-id}")
    private val clientId: String,
    private val cognitoClient: CognitoIdentityProviderClient,
) : CognitoConfirmSignupService {
    override fun handle(command: AuthCommands.ConfirmSignup) {
        val request =
            ConfirmSignUpRequest.builder()
                .clientId(clientId)
                .username(command.emailAddress)
                .confirmationCode(command.confirmCode)
                .build()

        cognitoClient.confirmSignUp(request)
    }
}
