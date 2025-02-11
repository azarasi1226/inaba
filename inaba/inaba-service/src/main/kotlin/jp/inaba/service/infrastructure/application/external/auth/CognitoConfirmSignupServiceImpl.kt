package jp.inaba.service.infrastructure.application.external.auth

import jp.inaba.core.domain.common.UseCaseException
import jp.inaba.service.application.external.auth.confirmsignup.CognitoConfirmSignupService
import jp.inaba.service.application.external.auth.confirmsignup.ConfirmSignupError
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient
import software.amazon.awssdk.services.cognitoidentityprovider.model.CodeMismatchException
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmSignUpRequest
import software.amazon.awssdk.services.cognitoidentityprovider.model.ExpiredCodeException
import software.amazon.awssdk.services.cognitoidentityprovider.model.NotAuthorizedException

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

        try {
            cognitoClient.confirmSignUp(request)
        } catch (e: CodeMismatchException) {
            throw UseCaseException(ConfirmSignupError.ConfirmCodeMismatch)
        } catch (e: ExpiredCodeException) {
            throw UseCaseException(ConfirmSignupError.ExpiredCode)
        } catch (e: NotAuthorizedException) {
            throw UseCaseException(ConfirmSignupError.NotAuthorized)
        }
    }
}
