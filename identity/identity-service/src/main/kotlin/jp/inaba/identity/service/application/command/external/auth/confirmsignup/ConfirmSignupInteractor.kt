package jp.inaba.identity.service.application.command.external.auth.confirmsignup

import jp.inaba.identity.api.domain.external.auth.AuthCommands
import jp.inaba.identity.api.domain.external.auth.AuthEvents
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventhandling.gateway.EventGateway
import org.springframework.stereotype.Component

@Component
class ConfirmSignupInteractor(
    private val cognitoConfirmSignupService: CognitoConfirmSignupService,
    private val eventGateway: EventGateway
) {
    @CommandHandler
    fun handle(command: AuthCommands.ConfirmSignup) {
        cognitoConfirmSignupService.handle(command)

        val event = AuthEvents.SignupConfirmed(
            emailAddress = command.emailAddress,
            confirmCode = command.confirmCode
        )

        eventGateway.publish(event)
    }
}