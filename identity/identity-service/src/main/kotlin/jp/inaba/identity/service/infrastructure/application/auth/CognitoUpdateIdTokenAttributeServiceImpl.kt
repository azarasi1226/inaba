package jp.inaba.identity.service.infrastructure.application.auth

import jp.inaba.identity.service.application.command.external.auth.updateidtokenattribute.CognitoUpdateIdTokenAttributeService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminUpdateUserAttributesRequest
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType

@Service
class CognitoUpdateIdTokenAttributeServiceImpl(
    @Value("\${aws.cognito.user-pool-id}")
    private val userPoolId: String,
    private val cognitoClient: CognitoIdentityProviderClient,
) : CognitoUpdateIdTokenAttributeService {
    override fun handle(emailAddress: String, attributeName: String, attributeContent: String) {
        val request =
            AdminUpdateUserAttributesRequest.builder()
                .userPoolId(userPoolId)
                .username(emailAddress)
                .userAttributes(
                    AttributeType.builder()
                        .name(attributeName)
                        .value(attributeContent)
                        .build(),
                )
                .build()

        cognitoClient.adminUpdateUserAttributes(request)
    }
}
