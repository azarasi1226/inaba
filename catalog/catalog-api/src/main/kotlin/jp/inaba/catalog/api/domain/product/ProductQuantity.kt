package jp.inaba.catalog.api.domain.product

data class ProductQuantity(val value: Int) {
    companion object{
        private const val MAX_QUANTITY = 99
    }
    init{
        if(value > MAX_QUANTITY){
            throw Exception("最大数量は${MAX_QUANTITY}です。現在の個数:${value}")
        }
    }
}