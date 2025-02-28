package jp.inaba.core.domain.brand

import jp.inaba.core.domain.common.UseCaseError

enum class FindBrandByIdError(
    override val errorCode: String,
    override val errorMessage: String,
) : UseCaseError {
    BRAND_NOD_FOUND("1", "ブランドが存在しません"),
}
