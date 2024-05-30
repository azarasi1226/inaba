package jp.inaba.catalog.api.domain.product

import jp.inaba.common.domain.shared.DomainException

data class ProductPrice(val value: Int) {
    companion object {
        private const val MIN = 1
        private const val MAX = 1_000_000_000
    }

    init {
        if (value !in MIN..MAX) {
            throw DomainException("商品価格は[$MIN~$MAX]の間です。現在の価格[$value]")
        }
    }
}
