package jp.inaba.identity.service.presentation.cognito.signup

import jp.inaba.identity.api.domain.external.cognito.CognitoCommands
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/signup")
class SignupController(
    private val commandGateway: CommandGateway
) {
    fun signup(request: SignupRequest) {
        val command = CognitoCommands.Signup(
            emailAddress = request.emailAddress,
            password = request.password
        )

        commandGateway.sendAndWait<Any>(command)
    }
}