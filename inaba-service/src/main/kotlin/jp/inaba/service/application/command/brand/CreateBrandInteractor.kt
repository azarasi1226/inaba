package jp.inaba.service.application.command.brand

import jp.inaba.core.domain.common.CommonError
import jp.inaba.core.domain.common.UseCaseException
import jp.inaba.message.brand.command.CreateBrandCommand
import jp.inaba.service.domain.UniqueAggregateIdVerifier
import jp.inaba.service.domain.brand.InternalCreateBrandCommand
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.stereotype.Component

@Component
class CreateBrandInteractor(
    private val uniqueAggregateIdVerifier: UniqueAggregateIdVerifier,
    private val commandGateway: CommandGateway,
) {
    @CommandHandler
    fun handle(command: CreateBrandCommand) {
        if (uniqueAggregateIdVerifier.hasDuplicateAggregateId(command.id.value)) {
            throw UseCaseException(CommonError.AGGREGATE_DUPLICATED)
        }

        val internalCommand =
            InternalCreateBrandCommand(
                id = command.id,
                name = command.name,
            )

        commandGateway.sendAndWait<Any>(internalCommand)
    }
}
