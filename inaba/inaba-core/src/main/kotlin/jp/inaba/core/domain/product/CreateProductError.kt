package jp.inaba.core.domain.product

import jp.inaba.core.domain.common.UseCaseError

enum class CreateProductError(
    override val errorCode: String,
    override val errorMessage: String,
) : UseCaseError {
    PRODUCT_ALREADY_EXISTS("1", "同じIDで商品が存在しています"),
}
