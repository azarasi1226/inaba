package jp.inaba.identity.service.infrastructure.application.auth

import jp.inaba.identity.api.domain.external.auth.AuthCommands
import jp.inaba.identity.service.application.command.external.auth.updateidtokenattribute.CognitoUpdateIdTokenAttributeForUserIdService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminUpdateUserAttributesRequest
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType

@Service
class CognitoUpdateIdTokenAttributeForUserIdServiceImpl(
    @Value("\${aws.cognito.user-pool-id}")
    private val userPoolId: String,
    private val cognitoClient: CognitoIdentityProviderClient,
) : CognitoUpdateIdTokenAttributeForUserIdService {
    override fun handle(command: AuthCommands.UpdateIdTokenAttributeForUserId) {
        val request =
            AdminUpdateUserAttributesRequest.builder()
                .userPoolId(userPoolId)
                .username(command.emailAddress)
                .userAttributes(
                    AttributeType.builder()
                        .name("custom:user_id")
                        .value(command.userId.value)
                        .build(),
                )
                .build()

        cognitoClient.adminUpdateUserAttributes(request)
    }
}
