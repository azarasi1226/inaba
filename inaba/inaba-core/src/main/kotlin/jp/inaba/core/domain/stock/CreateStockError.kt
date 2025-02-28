package jp.inaba.core.domain.stock

import jp.inaba.core.domain.common.UseCaseError

enum class CreateStockError(
    override val errorCode: String,
    override val errorMessage: String,
) : UseCaseError {
    PRODUCT_NOT_FOUND("1", "商品が存在しませんでした"),
    STOCK_ALREADY_LINKED_TO_PRODUCT("2", "別の在庫とすでにリンク済みの商品です"),
}
