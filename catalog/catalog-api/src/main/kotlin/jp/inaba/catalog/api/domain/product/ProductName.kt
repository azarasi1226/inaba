package jp.inaba.catalog.api.domain.product

data class ProductName(val value: String) {
    companion object {
        private const val MIN_LENGTH = 1
        private const val MAX_LENGTH = 70
    }

    init {
        if(value.length < MIN_LENGTH) {
            throw IllegalArgumentException("商品名が短すぎます")
        }

        if (value.length > MAX_LENGTH) {
            throw IllegalArgumentException("商品名が長すぎます")
        }
    }
}