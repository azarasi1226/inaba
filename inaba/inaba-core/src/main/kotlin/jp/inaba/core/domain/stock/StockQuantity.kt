package jp.inaba.core.domain.stock

import jp.inaba.core.domain.common.DomainException

data class StockQuantity(val value: Int) {
    companion object {
        private const val MIN = 0
        private const val MAX = 1_000_000
    }

    init {
        if (value !in MIN..MAX) {
            throw DomainException("在庫数は[$MIN~$MAX]の間です。value:[$value]")
        }
    }
}
