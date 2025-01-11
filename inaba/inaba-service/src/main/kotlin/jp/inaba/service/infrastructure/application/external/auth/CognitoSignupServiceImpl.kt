package jp.inaba.service.infrastructure.application.external.auth

import jp.inaba.service.application.external.auth.signup.CognitoSignupService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpRequest

@Service
class CognitoSignupServiceImpl(
    @Value("\${aws.cognito.client-id}")
    private val clientId: String,
    private val cognitoClient: CognitoIdentityProviderClient,
) : CognitoSignupService {
    override fun handle(
        emailAddress: String,
        password: String,
    ) {
        val request =
            SignUpRequest.builder()
                .clientId(clientId)
                .username(emailAddress)
                .password(password)
                // TODO(このUserAttribute本来であれば消せるはず...userNameでemailAddressを渡してるから。
                // local cognitoの時だけなぜかこのattribute無いとエラーになるから追加してるだけ。余裕があれば消したい)
                .userAttributes(
                    AttributeType.builder()
                        .name("email")
                        .value(emailAddress)
                        .build(),
                )
                .build()

        cognitoClient.signUp(request)
    }
}
