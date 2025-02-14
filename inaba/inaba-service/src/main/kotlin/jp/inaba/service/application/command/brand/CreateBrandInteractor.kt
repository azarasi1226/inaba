package jp.inaba.service.application.command.brand

import jp.inaba.core.domain.brand.CreateBrandError
import jp.inaba.core.domain.common.UseCaseException
import jp.inaba.message.brand.command.CreateBrandCommand
import jp.inaba.service.domain.brand.CreateBrandVerifier
import jp.inaba.service.domain.brand.InternalCreateBrandCommand
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Component

@Component
class CreateBrandInteractor(
    private val verifier: CreateBrandVerifier,
    private val commandGateway: CommandGateway,
) {
    @CommandHandler
    fun handle(command: CreateBrandCommand) {
        if(verifier.isBrandExits(command.id)) {
            throw UseCaseException(CreateBrandError.BRAND_ALREADY_EXISTS)
        }

        val internalCommand = InternalCreateBrandCommand(
            id = command.id,
            name = command.name
        )

        commandGateway.sendAndWait<Any>(internalCommand)
    }
}