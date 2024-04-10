package jp.inaba.identity.service.application.command.external.cognito.confirmsignup

import jp.inaba.identity.api.domain.external.cognito.CognitoCommands
import jp.inaba.identity.api.domain.external.cognito.CognitoEvents
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventhandling.gateway.EventGateway
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmSignUpRequest

@Service
class ConfirmSignupInteractor(
    @Value("aws.cognito.client_id")
    private val clientId: String,
    private val cognitoClient: CognitoIdentityProviderClient,
    private val eventGateway: EventGateway
) {
    @CommandHandler
    fun handle(command: CognitoCommands.ConfirmSignup) {
        val request = ConfirmSignUpRequest.builder()
            .clientId(clientId)
            .username(command.emailAddress)
            .confirmationCode(command.confirmCode)
            .build()

        val response = cognitoClient.confirmSignUp(request)

        val event = CognitoEvents.SignupConfirmed(
            sub = TODO(),
            emailAddress = command.emailAddress,
            confirmCode = command.confirmCode)

        eventGateway.publish(event)
    }
}