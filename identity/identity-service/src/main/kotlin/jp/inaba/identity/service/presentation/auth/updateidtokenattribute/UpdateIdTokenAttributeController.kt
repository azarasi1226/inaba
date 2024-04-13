package jp.inaba.identity.service.presentation.auth.updateidtokenattribute

import jp.inaba.identity.api.domain.external.auth.AuthCommands
import jp.inaba.identity.api.domain.external.auth.updateIdTokenAttribute
import jp.inaba.identity.service.presentation.auth.AuthControllerBase
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UpdateIdTokenAttributeController(
    private val commandGateway: CommandGateway
) : AuthControllerBase() {
    @PostMapping("/update-id-token-attribute")
    fun handle(
        @RequestBody
        request: UpdateIdTokenAttributeRequest
    ) {
        val command = AuthCommands.UpdateIdTokenAttribute(
            emailAddress = request.emailAddress,
            idTokenAttributes = request.idTokenAttributes
        )

        commandGateway.updateIdTokenAttribute(command)
    }
}