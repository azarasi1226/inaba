package jp.inaba.identity.service.application.command.external.auth.updateidtokenattribute

import jp.inaba.identity.api.domain.external.auth.IdTokenAttributeForBasketIdUpdatedEvent
import jp.inaba.identity.api.domain.external.auth.IdTokenAttributeForUserIdUpdatedEvent
import jp.inaba.identity.api.domain.external.auth.UpdateIdTokenAttributeForBasketIdCommand
import jp.inaba.identity.api.domain.external.auth.UpdateIdTokenAttributeForUserIdCommand
import jp.inaba.identity.service.domain.auth.IdTokenAttributeName
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventhandling.gateway.EventGateway
import org.springframework.stereotype.Component

@Component
class UpdateIdTokenAttributeInteractor(
    private val cognitoUpdateIdTokenAttributeService: CognitoUpdateIdTokenAttributeService,
    private val eventGateway: EventGateway,
) {
    @CommandHandler
    fun handle(command: UpdateIdTokenAttributeForUserIdCommand) {
        cognitoUpdateIdTokenAttributeService.handle(
            emailAddress = command.emailAddress,
            attributeName = IdTokenAttributeName.UserId.value,
            attributeContent = command.userId.value
        )

        val event =
            IdTokenAttributeForUserIdUpdatedEvent(
                emailAddress = command.emailAddress,
                userId = command.userId,
            )

        eventGateway.publish(event)
    }

    @CommandHandler
    fun handle(command: UpdateIdTokenAttributeForBasketIdCommand) {
        cognitoUpdateIdTokenAttributeService.handle(
            emailAddress = command.emailAddress,
            attributeName = IdTokenAttributeName.BasketId.value,
            attributeContent = command.basketId.value
        )

        val event =
            IdTokenAttributeForBasketIdUpdatedEvent(
                emailAddress = command.emailAddress,
                basketId = command.basketId,
            )

        eventGateway.publish(event)
    }
}
