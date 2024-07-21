package jp.inaba.identity.service.infrastructure.application.auth

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
    override fun handle(
        emailAddress: String,
        confirmCode: String,
    ) {
        val request =
            ConfirmSignUpRequest.builder()
                .clientId(clientId)
                .username(emailAddress)
                .confirmationCode(confirmCode)
                .build()

        cognitoClient.confirmSignUp(request)
    }
}
