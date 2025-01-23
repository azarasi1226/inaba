package jp.inaba.core.domain.stock

import jp.inaba.core.domain.common.DomainError
import jp.inaba.core.domain.common.DomainException

enum class IncreaseStockError(
    override val errorCode: String,
    override val errorMessage: String
) : DomainError {
    OutOfStock("1", "在庫が満杯です。")
}