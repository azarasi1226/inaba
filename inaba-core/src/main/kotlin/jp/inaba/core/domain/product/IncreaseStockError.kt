package jp.inaba.core.domain.product

import jp.inaba.core.domain.common.UseCaseError

enum class IncreaseStockError(
    override val errorCode: String,
    override val errorMessage: String,
) : UseCaseError {
    CANNOT_RECEIVE("1", "在庫が満杯のためこれ以上入れられません"),
}
