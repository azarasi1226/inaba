package jp.inaba.identity.service.presentation.auth.confirmsignup

import jp.inaba.identity.api.domain.external.auth.ConfirmSignupCommand
import jp.inaba.identity.api.domain.external.auth.confirmSignup
import jp.inaba.identity.service.presentation.auth.AuthController
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ConfirmSignupController(
    private val commandGateway: CommandGateway,
) : AuthController {
    @PostMapping("/confirm-signup")
    fun handle(
        @RequestBody
        request: ConfirmSignupRequest,
    ) {
        val command =
            ConfirmSignupCommand(
                emailAddress = request.emailAddress,
                confirmCode = request.confirmCode,
            )

        commandGateway.confirmSignup(command)
    }
}
