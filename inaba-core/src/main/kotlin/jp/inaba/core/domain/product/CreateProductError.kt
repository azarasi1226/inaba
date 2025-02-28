package jp.inaba.core.domain.product

import jp.inaba.core.domain.common.UseCaseError

enum class CreateProductError(
    override val errorCode: String,
    override val errorMessage: String,
) : UseCaseError {
    BRAND_NOT_FOUND("1", "ブランドが存在しません"),
}
