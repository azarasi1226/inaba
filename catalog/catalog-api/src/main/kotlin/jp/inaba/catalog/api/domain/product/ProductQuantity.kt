package jp.inaba.catalog.api.domain.product

data class ProductQuantity(val value: Int) {
    companion object{
        private const val MAX = 99
    }
    init{
        if(value in 0.. MAX){
            throw Exception("最大数量は${MAX}です。現在の個数:${value}")
        }
    }
}