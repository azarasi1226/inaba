package jp.inaba.identity.service.application.command.external.auth.deleteauthuser

import jp.inaba.identity.api.domain.external.auth.AuthUserDeletedEvent
import jp.inaba.identity.api.domain.external.auth.DeleteAuthUserCommand
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventhandling.gateway.EventGateway
import org.springframework.stereotype.Component

@Component
class DeleteAuthUserInteractor(
    private val cognitoDeleteAuthUserService: CognitoDeleteAuthUserService,
    private val eventGateway: EventGateway,
) {
    @CommandHandler
    fun handle(command: DeleteAuthUserCommand) {
        cognitoDeleteAuthUserService.handle(command.emailAddress)

        val event = AuthUserDeletedEvent(command.emailAddress)

        eventGateway.publish(event)
    }
}
