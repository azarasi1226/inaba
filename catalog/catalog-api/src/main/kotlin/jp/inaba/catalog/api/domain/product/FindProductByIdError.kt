package jp.inaba.catalog.api.domain.product

import jp.inaba.common.domain.shared.DomainError

enum class FindProductByIdError(
    override val errorCode: String,
    override val errorMessage: String,
) : DomainError {
    PRODUCT_NOT_FOUND("1", "商品が見つかりません"),
}
