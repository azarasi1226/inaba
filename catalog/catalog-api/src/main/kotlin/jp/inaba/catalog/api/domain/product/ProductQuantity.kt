package jp.inaba.catalog.api.domain.product

import jp.inaba.common.domain.shared.DomainException

data class ProductQuantity(val value: Int) {
    companion object{
        private const val MIN = 0
        private const val MAX = 1_000_000
    }
    init{
        if(value !in MIN.. MAX){
            throw DomainException("商品個数は[${MIN}~${MAX}]の間です。現在の個数[${value}]")
        }
    }
}