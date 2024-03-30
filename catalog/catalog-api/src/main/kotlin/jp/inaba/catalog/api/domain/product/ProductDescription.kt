package jp.inaba.catalog.api.domain.product

data class ProductDescription(val value:String) {
    companion object {
        private const val PRODUCT_DESCRIPTION_MAX_LENGTH = 999
    }
    init{
        // Todo 最低文字数はあとで決める
        if(value.length > PRODUCT_DESCRIPTION_MAX_LENGTH) {
            throw Exception("商品概要は${PRODUCT_DESCRIPTION_MAX_LENGTH}文字以下にしてください。\r現在の文字数:${value.length}")
        }
    }
}