package jp.inaba.identity.api.domain.external.auth.command

import jp.inaba.basket.share.domain.basket.BasketId

data class UpdateIdTokenAttributeForBasketIdCommand(
    override val emailAddress: String,
    val basketId: BasketId,
) : AuthCommand