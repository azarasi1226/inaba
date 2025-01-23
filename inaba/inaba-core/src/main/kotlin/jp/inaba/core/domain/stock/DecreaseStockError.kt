package jp.inaba.core.domain.stock

import jp.inaba.core.domain.common.DomainError

enum class DecreaseStockError(
    override val errorCode: String,
    override val errorMessage: String
) : DomainError {
    InsufficientStock("1", "在庫を確保できませんでした")
}
