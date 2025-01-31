package jp.inaba.core.domain.stock

import jp.inaba.core.domain.common.UseCaseError

enum class CreateStockError(
    override val errorCode: String,
    override val errorMessage: String,
) : UseCaseError {
    ProductNotExits("1", "商品が存在しませんでした。"),
}
