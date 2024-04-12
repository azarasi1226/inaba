package jp.inaba.identity.service.presentation.auth.resendconfirmcode

import jp.inaba.identity.api.domain.external.auth.AuthCommands
import jp.inaba.identity.api.domain.external.auth.resendConfirmCode
import jp.inaba.identity.service.presentation.auth.AuthControllerBase
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ResendConfirmCodeController(
    private val commandGateway: CommandGateway
) : AuthControllerBase() {
    @PostMapping("/resend-confirm-code")
    fun resendConfirmCode(
        request: ResendConfirmCodeRequest
    ) {
        val command = AuthCommands.ResendConfirmCode(request.emailAddress)

        commandGateway.resendConfirmCode(command)
    }
}