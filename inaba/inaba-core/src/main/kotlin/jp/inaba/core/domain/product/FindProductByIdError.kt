package jp.inaba.core.domain.product

import jp.inaba.core.domain.common.DomainError

enum class FindProductByIdError(
    override val errorCode: String,
    override val errorMessage: String,
) : DomainError {
    PRODUCT_NOT_FOUND("1", "商品が見つかりません"),
}
