package jp.inaba.message.auth.command

import jp.inaba.core.domain.basket.BasketId

data class UpdateIdTokenAttributeForBasketIdCommand(
    override val emailAddress: String,
    val basketId: BasketId,
) : AuthCommand
