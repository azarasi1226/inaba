package jp.inaba.service.infrastructure.application.external.auth

import jp.inaba.service.application.external.auth.getauthuser.CognitoGetAuthUserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminGetUserRequest

@Service
class CognitoGetAuthUserServiceImpl(
    @Value("\${aws.cognito.user-pool-id}")
    private val userPoolId: String,
    private val cognitoClient: CognitoIdentityProviderClient,
) : CognitoGetAuthUserService {
    override fun handle(emailAddress: String): String {
        val request =
            AdminGetUserRequest.builder()
                .userPoolId(userPoolId)
                .username(emailAddress)
                .build()

        val response = cognitoClient.adminGetUser(request)

        return response.userAttributes().first { it.name() == "sub" }.value()
    }
}
