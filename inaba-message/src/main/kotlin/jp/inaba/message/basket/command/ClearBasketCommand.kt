package jp.inaba.message.basket.command

import jp.inaba.core.domain.basket.BasketId

data class ClearBasketCommand(
    override val id: BasketId,
) : BasketAggregateCommand
