package jp.inaba.catalog.api.domain.product

data class ProductName(val value: String) {
    companion object {
        private const val MIN_LENGTH = 1
        private const val MAX_LENGTH = 70
    }

    init {
        if(value.length < MIN_LENGTH) {
            throw IllegalArgumentException("商品名が短すぎます。　最小:${MIN_LENGTH}")
        }

        if (value.length > MAX_LENGTH) {
            throw IllegalArgumentException("商品名が長すぎます。　最大:${MAX_LENGTH} 現在:${value.length}")
        }
    }
}