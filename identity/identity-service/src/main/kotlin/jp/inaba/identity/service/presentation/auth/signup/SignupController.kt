package jp.inaba.identity.service.presentation.auth.signup

import jp.inaba.identity.api.domain.external.auth.AuthCommands
import jp.inaba.identity.api.domain.external.auth.signup
import jp.inaba.identity.service.presentation.auth.AuthControllerBase
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class SignupController(
    private val commandGateway: CommandGateway,
) : AuthControllerBase() {
    @PostMapping("/signup")
    fun handle(
        @RequestBody
        request: SignupRequest,
    ) {
        val command =
            AuthCommands.Signup(
                emailAddress = request.emailAddress,
                password = request.password,
            )

        commandGateway.signup(command)
    }
}
