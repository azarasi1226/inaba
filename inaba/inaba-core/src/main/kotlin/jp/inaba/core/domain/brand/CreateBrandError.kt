package jp.inaba.core.domain.brand

import jp.inaba.core.domain.common.UseCaseError

enum class CreateBrandError(
    override val errorCode: String,
    override val errorMessage: String,
) : UseCaseError {
    BRAND_ALREADY_EXISTS("1", "同じIDでブランドが存在しています"),
}
