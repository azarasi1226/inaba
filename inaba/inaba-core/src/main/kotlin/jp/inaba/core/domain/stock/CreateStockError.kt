package jp.inaba.core.domain.stock

import jp.inaba.core.domain.common.UseCaseError

enum class CreateStockError(
    override val errorCode: String,
    override val errorMessage: String,
) : UseCaseError {
    STOCK_ALREADY_EXISTS("1", "同じIDで在庫が存在しています"),
    PRODUCT_NOT_FOUND("2", "商品が存在しませんでした"),
    STOCK_ALREADY_LINKED_TO_PRODUCT("3", "別の在庫とすでにリンク済みの商品です"),
}
