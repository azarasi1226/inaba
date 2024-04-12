package jp.inaba.identity.service.application.command.external.auth.updateidtokenattribute

import jp.inaba.identity.api.domain.external.auth.AuthCommands
import jp.inaba.identity.api.domain.external.auth.AuthEvents
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventhandling.gateway.EventGateway
import org.springframework.stereotype.Component

@Component
class UpdateIdTokenAttributeInteractor(
    private val cognitoUpdateIdTokenAttributeService: CognitoUpdateIdTokenAttributeService,
    private val eventGateway: EventGateway
) {
    @CommandHandler
    fun handle(command: AuthCommands.UpdateIdTokenAttribute) {
        cognitoUpdateIdTokenAttributeService.handle(command)

        val event = AuthEvents.IdTokenAttributeUpdated(
            emailAddress = command.emailAddress,
            userId = command.userId.value,
        )

        eventGateway.publish(event)
    }
}