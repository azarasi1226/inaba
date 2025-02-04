package jp.inaba.service.application.saga.usersetup

import io.github.oshai.kotlinlogging.KotlinLogging
import jp.inaba.message.user.command.LinkSubjectCommand
import org.axonframework.commandhandling.gateway.CommandGateway

private val logger = KotlinLogging.logger {}

class LinkSubjectStep(
    private val commandGateway: CommandGateway
) {
    fun handle(
        command: LinkSubjectCommand,
        onFail: () -> Unit,
    ) {
        try {
            commandGateway.sendAndWait<Any>(command)
        } catch(e: Exception) {
            logger.error { "Subjectの紐づけに失敗しました exception:[$e]" }
            onFail.invoke()
        }
    }
}