package jp.inaba.service.domain.basket

import jp.inaba.core.domain.basket.BasketId
import jp.inaba.core.domain.user.UserId
import jp.inaba.message.basket.command.BasketAggregateCommand

data class InternalCreateBasketCommand(
    override val id: BasketId,
    val userId: UserId,
) : BasketAggregateCommand