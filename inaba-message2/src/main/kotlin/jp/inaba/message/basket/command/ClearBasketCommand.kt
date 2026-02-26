package jp.inaba.message.basket.command

import jp.inaba.core.domain.user.UserId
import jp.inaba.message.UseCaseResult
import org.axonframework.modelling.annotation.TargetEntityId

data class ClearBasketCommand(
    @TargetEntityId
    val id: UserId,
)

class ClearBasketResult private constructor(
    override val success: Boolean,
    override val error: jp.inaba.message.Error?,
) : UseCaseResult() {
    companion object {
        fun success(): ClearBasketResult = ClearBasketResult(true, null)
        fun userNotFound(): ClearBasketResult = ClearBasketResult(false, jp.inaba.message.Error("ユーザーが存在しません"))
    }
}
