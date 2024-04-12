package jp.inaba.basket.api.domain.basket

//TODO(もっとちゃんと考えろ)
object BasketErrors {
    enum class Create(
        val errorCode: String,
        val errorMessage: String
    ) {
        USER_NOT_FOUND("B_B_C_001", "ユーザーが存在しませんでした")
    }

    enum class SetBasketItem(
        val errorCode: String,
        val errorMessage: String
    ) {
        PRODUCT_NOT_FOUND("B_B_S_001", "商品が見つかりませんでした"),
        PRODUCT_MAX_KIND_OVER("B_B_S_002", "商品種類の上限数に到達しました");
    }
}