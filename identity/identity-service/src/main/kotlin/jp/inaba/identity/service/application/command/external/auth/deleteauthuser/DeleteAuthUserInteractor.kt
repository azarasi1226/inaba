package jp.inaba.identity.service.application.command.external.auth.deleteauthuser

import jp.inaba.identity.api.domain.external.auth.AuthCommands
import jp.inaba.identity.api.domain.external.auth.AuthEvents
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventhandling.gateway.EventGateway
import org.springframework.stereotype.Component

@Component
class DeleteAuthUserInteractor(
    private val cognitoDeleteAuthUserService: CognitoDeleteAuthUserService,
    private val eventGateway: EventGateway,
) {
    @CommandHandler
    fun handle(command: AuthCommands.DeleteAuthUser) {
        cognitoDeleteAuthUserService.handle(command)

        val event = AuthEvents.AuthUserDeleted(command.emailAddress)

        eventGateway.publish(event)
    }
}
