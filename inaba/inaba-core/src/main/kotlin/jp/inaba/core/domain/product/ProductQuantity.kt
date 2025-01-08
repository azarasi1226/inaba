package jp.inaba.core.domain.product

import jp.inaba.core.domain.common.DomainException

data class ProductQuantity(val value: Int) {
    companion object {
        private const val MIN = 0
        private const val MAX = 1_000_000
    }

    init {
        if (value !in MIN..MAX) {
            throw DomainException("商品個数は[$MIN~$MAX]の間です。value:[$value]")
        }
    }
}
