package jp.inaba.catalog.api.domain.product

data class ProductPrice(val value: Int) {
    companion object{
        private const val PRICE_CAP = 1000_000_000
    }

    init {
        if (value !in 1..PRICE_CAP) {
            throw IllegalArgumentException("価格が不正です 1 ~ ${PRICE_CAP}の範囲を指定してください。　price : ${value}}")
        }
    }
}
