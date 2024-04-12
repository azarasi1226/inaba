package jp.inaba.identity.service.application.command.external.auth.signup

import jp.inaba.identity.api.domain.external.auth.AuthCommands
import jp.inaba.identity.api.domain.external.auth.AuthEvents
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventhandling.gateway.EventGateway
import org.springframework.stereotype.Component

@Component
class SignupInteractor(
    private val cognitoSignupService: CognitoSignupService,
    private val eventGateway: EventGateway
) {
    @CommandHandler
    fun handle(command: AuthCommands.Signup) {
        cognitoSignupService.handle(command)

        val event = AuthEvents.Signedup(
            emailAddress = command.emailAddress,
            password = command.password
        )

        eventGateway.publish(event)
    }
}