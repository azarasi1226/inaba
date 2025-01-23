package jp.inaba.core.domain.stock

import jp.inaba.core.domain.common.DomainError

enum class IncreaseStockError(
    override val errorCode: String,
    override val errorMessage: String,
) : DomainError {
    OutOfStock("1", "在庫が満杯です。"),
}
