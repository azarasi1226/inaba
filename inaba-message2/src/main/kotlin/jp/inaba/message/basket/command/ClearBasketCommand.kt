package jp.inaba.message.basket.command

import jp.inaba.core.domain.user.UserId
import org.axonframework.modelling.annotation.TargetEntityId

data class ClearBasketCommand(
    @TargetEntityId
    val id: UserId,
)
