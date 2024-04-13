package jp.inaba.identity.service.infrastructure.application.auth

import jp.inaba.identity.api.domain.external.auth.AuthCommands
import jp.inaba.identity.service.application.command.external.auth.signup.CognitoSignupService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpRequest

@Service
class CognitoSignupServiceImpl(
    @Value("\${aws.cognito.client-id}")
    private val clientId: String,
    private val cognitoClient: CognitoIdentityProviderClient,
) : CognitoSignupService {
    override fun handle(command: AuthCommands.Signup) {
        val request = SignUpRequest.builder()
            .clientId(clientId)
            .username(command.emailAddress)
            .password(command.password)
            .build()

        cognitoClient.signUp(request)
    }
}