package jp.inaba.message.basket.command

import jp.inaba.core.domain.basket.BasketId

data class DeleteBasketCommand(
    override val id: BasketId,
) : BasketAggregateCommand
