package jp.inaba.identity.service.presentation.auth.confirmsignup

import jp.inaba.identity.api.domain.external.auth.AuthCommands
import jp.inaba.identity.api.domain.external.auth.confirmSignup
import jp.inaba.identity.service.presentation.auth.AuthControllerBase
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ConfirmSignupController(
    private val commandGateway: CommandGateway,
) : AuthControllerBase() {
    @PostMapping("/confirm-signup")
    fun handle(
        @RequestBody
        request: ConfirmSignupRequest,
    ) {
        val command =
            AuthCommands.ConfirmSignup(
                emailAddress = request.emailAddress,
                confirmCode = request.confirmCode,
            )

        commandGateway.confirmSignup(command)
    }
}
