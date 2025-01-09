package jp.inaba.service.infrastructure.application.external.auth

import jp.inaba.service.application.external.auth.deleteauthuser.CognitoDeleteAuthUserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminDeleteUserRequest

@Service
class CognitoDeleteAuthUserServiceImpl(
    @Value("\${aws.cognito.user-pool-id}")
    private val userPoolId: String,
    private val cognitoIdentityProviderClient: CognitoIdentityProviderClient,
) : CognitoDeleteAuthUserService {
    override fun handle(emailAddress: String) {
        val request =
            AdminDeleteUserRequest.builder()
                .userPoolId(userPoolId)
                .username(emailAddress)
                .build()

        cognitoIdentityProviderClient.adminDeleteUser(request)
    }
}
