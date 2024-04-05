package jp.inaba.identity.service.application.command.external.cognito.signup

import org.axonframework.commandhandling.CommandHandler
import org.springframework.stereotype.Service
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

@Service
class SignupCognitoInteractor(
) {
    @CommandHandler
    fun handle(command: JvmType.Object) {

    }
}