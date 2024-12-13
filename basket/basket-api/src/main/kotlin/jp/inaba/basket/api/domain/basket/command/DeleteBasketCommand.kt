package jp.inaba.basket.api.domain.basket.command

import jp.inaba.basket.share.domain.basket.BasketId

data class DeleteBasketCommand(
    override val id: BasketId,
) : BasketAggregateCommand
