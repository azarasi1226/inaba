package jp.inaba.core.domain.product

import jp.inaba.core.domain.common.UseCaseError

enum class FindProductByIdError(
    override val errorCode: String,
    override val errorMessage: String,
) : UseCaseError {
    PRODUCT_NOT_FOUND("1", "商品が見つかりません"),
}
