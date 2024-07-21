package jp.inaba.identity.service.application.command.external.auth.confirmsignup

import jp.inaba.identity.api.domain.external.auth.ConfirmSignupCommand
import jp.inaba.identity.api.domain.external.auth.SignupConfirmedEvent
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventhandling.gateway.EventGateway
import org.springframework.stereotype.Component

@Component
class ConfirmSignupInteractor(
    private val cognitoConfirmSignupService: CognitoConfirmSignupService,
    private val eventGateway: EventGateway,
) {
    @CommandHandler
    fun handle(command: ConfirmSignupCommand) {
        cognitoConfirmSignupService.handle(
            emailAddress = command.emailAddress,
            confirmCode = command.confirmCode
        )

        val event =
            SignupConfirmedEvent(
                emailAddress = command.emailAddress,
                confirmCode = command.confirmCode,
            )

        eventGateway.publish(event)
    }
}
