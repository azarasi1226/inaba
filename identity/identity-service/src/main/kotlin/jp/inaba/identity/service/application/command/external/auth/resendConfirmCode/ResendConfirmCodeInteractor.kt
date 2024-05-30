package jp.inaba.identity.service.application.command.external.auth.resendConfirmCode

import jp.inaba.identity.api.domain.external.auth.AuthCommands
import jp.inaba.identity.api.domain.external.auth.AuthEvents
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventhandling.gateway.EventGateway
import org.springframework.stereotype.Component

@Component
class ResendConfirmCodeInteractor(
    private val cognitoResendConfirmCodeService: CognitoResendConfirmCodeService,
    private val eventGateway: EventGateway,
) {
    @CommandHandler
    fun handle(command: AuthCommands.ResendConfirmCode) {
        cognitoResendConfirmCodeService.handle(command)

        val event =
            AuthEvents.ConfirmCodeResent(
                emailAddress = command.emailAddress,
            )

        eventGateway.publish(event)
    }
}
