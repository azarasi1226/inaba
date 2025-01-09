package jp.inaba.service.application.external.auth.confirmsignup

import jp.inaba.message.auth.command.ConfirmSignupCommand
import jp.inaba.message.auth.event.SignupConfirmedEvent
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
            confirmCode = command.confirmCode,
        )

        val event =
            SignupConfirmedEvent(
                emailAddress = command.emailAddress,
                confirmCode = command.confirmCode,
            )

        eventGateway.publish(event)
    }
}
