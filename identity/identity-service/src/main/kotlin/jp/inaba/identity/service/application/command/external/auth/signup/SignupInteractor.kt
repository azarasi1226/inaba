package jp.inaba.identity.service.application.command.external.auth.signup

import jp.inaba.identity.api.domain.external.auth.SignedupEvent
import jp.inaba.identity.api.domain.external.auth.SignupCommand
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventhandling.gateway.EventGateway
import org.springframework.stereotype.Component

@Component
class SignupInteractor(
    private val cognitoSignupService: CognitoSignupService,
    private val eventGateway: EventGateway,
) {
    @CommandHandler
    fun handle(command: SignupCommand) {
        cognitoSignupService.handle(
            emailAddress = command.emailAddress,
            password = command.password,
        )

        val event =
            SignedupEvent(
                emailAddress = command.emailAddress,
                password = command.password,
            )

        eventGateway.publish(event)
    }
}
