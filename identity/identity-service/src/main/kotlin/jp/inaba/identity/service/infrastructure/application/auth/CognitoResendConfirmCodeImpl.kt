package jp.inaba.identity.service.infrastructure.application.auth

import jp.inaba.identity.api.domain.external.auth.AuthCommands
import jp.inaba.identity.service.application.command.external.auth.resendConfirmCode.CognitoResendConfirmCodeService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient
import software.amazon.awssdk.services.cognitoidentityprovider.model.ResendConfirmationCodeRequest

@Service
class CognitoResendConfirmCodeImpl(
    @Value("aws.cognito.client_id")
    private val clientId: String,
    private val cognitoClient: CognitoIdentityProviderClient
) : CognitoResendConfirmCodeService {
    override fun handle(command: AuthCommands.ResendConfirmCode) {
        val request = ResendConfirmationCodeRequest.builder()
            .clientId(clientId)
            .username(command.emailAddress)
            .build()

        cognitoClient.resendConfirmationCode(request)
    }
}