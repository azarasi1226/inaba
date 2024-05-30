package jp.inaba.identity.service.infrastructure.application.auth

import jp.inaba.identity.api.domain.external.auth.AuthCommands
import jp.inaba.identity.service.application.command.external.auth.deleteauthuser.CognitoDeleteAuthUserService
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
    override fun handle(command: AuthCommands.DeleteAuthUser) {
        val request =
            AdminDeleteUserRequest.builder()
                .userPoolId(userPoolId)
                .username(command.emailAddress)
                .build()

        cognitoIdentityProviderClient.adminDeleteUser(request)
    }
}
