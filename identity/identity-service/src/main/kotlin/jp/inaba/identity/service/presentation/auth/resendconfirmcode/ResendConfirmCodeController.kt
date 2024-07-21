package jp.inaba.identity.service.presentation.auth.resendconfirmcode

import jp.inaba.identity.api.domain.external.auth.ResendConfirmCodeCommand
import jp.inaba.identity.api.domain.external.auth.resendConfirmCode
import jp.inaba.identity.service.presentation.auth.AuthController
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ResendConfirmCodeController(
    private val commandGateway: CommandGateway,
) : AuthController {
    @PostMapping("/resend-confirm-code")
    fun handle(request: ResendConfirmCodeRequest) {
        val command = ResendConfirmCodeCommand(request.emailAddress)

        commandGateway.resendConfirmCode(command)
    }
}
