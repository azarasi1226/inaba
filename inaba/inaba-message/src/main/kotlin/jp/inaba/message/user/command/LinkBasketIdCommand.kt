package jp.inaba.message.user.command

import jp.inaba.core.domain.basket.BasketId
import jp.inaba.core.domain.user.UserId

data class LinkBasketIdCommand(
    override val id: UserId,
    val basketId: BasketId,
) : UserAggregateCommand
