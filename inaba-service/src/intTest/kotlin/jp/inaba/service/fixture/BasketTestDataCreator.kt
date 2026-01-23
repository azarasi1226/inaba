package jp.inaba.service.fixture

import jp.inaba.core.domain.basket.BasketId
import jp.inaba.core.domain.user.UserId
import jp.inaba.service.domain.basket.InternalCreateBasketCommand
import org.axonframework.commandhandling.gateway.CommandGateway

class BasketTestDataCreator(
    private val commandGateway: CommandGateway,
) {
    fun create(
        id: BasketId = BasketId(),
        userId: UserId = UserId(),
    ): BasketId {
        val command =
            InternalCreateBasketCommand(
                id = id,
                userId = userId,
            )

        commandGateway.sendAndWait<Any>(command)

        return id
    }
}
