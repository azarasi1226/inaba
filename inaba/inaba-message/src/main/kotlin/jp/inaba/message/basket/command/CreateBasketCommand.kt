package jp.inaba.message.basket.command

import jp.inaba.core.domain.basket.BasketId
import jp.inaba.core.domain.user.UserId
import org.axonframework.commandhandling.RoutingKey

data class CreateBasketCommand(
    @get:RoutingKey
    val id: BasketId,
    val userId: UserId,
)
