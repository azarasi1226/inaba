package jp.inaba.core.domain.product

import jp.inaba.core.domain.common.UseCaseError

enum class DecreaseStockError(
    override val errorCode: String,
    override val errorMessage: String,
) : UseCaseError {
    INSUFFICIENT_STOCK("1", "在庫が確保できませんでした"),
}
