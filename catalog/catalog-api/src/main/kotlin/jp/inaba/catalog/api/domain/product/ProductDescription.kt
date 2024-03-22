package jp.inaba.catalog.api.domain.product

data class ProductDescription(val value:String) {
    companion object {
        private const val PRODUCT_DESCRIPTION_MAX_LENGTH = 9999
        private const val PRODUCT_DESCRIPTION_MIN_LENGTH = 1
    }
    init{
        if(value.length >= PRODUCT_DESCRIPTION_MIN_LENGTH){
            throw Exception("商品概要は${PRODUCT_DESCRIPTION_MIN_LENGTH}文字以上入力してください。")
        }
    }
}