package jp.inaba.message.basket.command

import jp.inaba.core.domain.basket.BasketItemQuantity
import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.user.UserId
import jp.inaba.message.Error
import jp.inaba.message.UseCaseResult
import org.axonframework.modelling.annotation.TargetEntityId

data class SetBasketItemCommand(
    val id: UserId,
    val productId: ProductId,
    val basketItemQuantity: BasketItemQuantity,
) {
    data class TargetId(val userId: UserId, val productId: ProductId)

    @get:TargetEntityId
    private val targetId: TargetId
        get() = TargetId(userId = id, productId = productId)
}

class SetBasketItemResult private constructor(
    override val success: Boolean,
    override val error: Error?,
) : UseCaseResult() {
    companion object {
        fun success(): SetBasketItemResult = SetBasketItemResult(true, null)
        fun userNotFound(): SetBasketItemResult = SetBasketItemResult(false, Error("ユーザーが存在しません"))
        fun productNotFound(): SetBasketItemResult = SetBasketItemResult(false, Error("商品が存在しません"))
        fun productMaxKindOver(): SetBasketItemResult = SetBasketItemResult(false, Error("商品種類の上限数に到達しました"))
    }
}
