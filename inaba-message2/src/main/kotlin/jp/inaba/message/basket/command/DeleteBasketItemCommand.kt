package jp.inaba.message.basket.command

import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.user.UserId
import jp.inaba.message.Error
import jp.inaba.message.UseCaseResult
import org.axonframework.modelling.annotation.TargetEntityId

data class DeleteBasketItemCommand(
    @TargetEntityId
    val id: UserId,
    val productId: ProductId,
)

class DeleteBasketItemResult private constructor(
    override val success: Boolean,
    override val error: Error?,
) : UseCaseResult() {
    companion object {
        fun success(): DeleteBasketItemResult = DeleteBasketItemResult(true, null)
        fun userNotFound(): DeleteBasketItemResult = DeleteBasketItemResult(false, Error("ユーザーが存在しません"))
    }
}
