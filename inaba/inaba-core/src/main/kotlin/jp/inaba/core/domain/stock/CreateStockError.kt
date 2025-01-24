package jp.inaba.core.domain.stock

import jp.inaba.core.domain.common.DomainError

enum class CreateStockError(
    override val errorCode: String,
    override val errorMessage: String,
) : DomainError {
    ProductNotExits("1", "商品が存在しませんでした。"),
}
