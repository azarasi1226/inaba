package jp.inaba.service.application.external.auth.deleteauthuser

import jp.inaba.message.auth.command.DeleteAuthUserCommand
import jp.inaba.message.auth.event.AuthUserDeletedEvent
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
