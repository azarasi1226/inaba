package jp.inaba.basket.api.domain.basket.command

import jp.inaba.basket.share.domain.basket.BasketId
import jp.inaba.identity.api.domain.user.UserId
import org.axonframework.commandhandling.RoutingKey

data class CreateBasketCommand(
    @get:RoutingKey
    val id: BasketId,
    val userId: UserId,
)