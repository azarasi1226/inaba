package jp.inaba.core.domain.product

import jp.inaba.core.domain.common.UseCaseError

enum class ShipmentProductError(
    override val errorCode: String,
    override val errorMessage: String,
) : UseCaseError {
    OutOfStock("1", "在庫を確保できませんでした"),
}
