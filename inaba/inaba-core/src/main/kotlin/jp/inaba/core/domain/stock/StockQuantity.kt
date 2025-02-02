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

    fun canNotAdd(increaseCount: IncreaseCount): Boolean {
        val newValue = value + increaseCount.value

        return newValue > MAX
    }

    fun add(increaseCount: IncreaseCount): StockQuantity {
        if (canNotAdd(increaseCount)) {
            throw DomainException("在庫数の上限は[$MAX]です。")
        }

        val newValue = value + increaseCount.value
        return StockQuantity(newValue)
    }

    fun canNotSubtract(decreaseCount: DecreaseCount): Boolean {
        val newValue = value - decreaseCount.value

        return newValue < MIN
    }

    fun subtract(decreaseCount: DecreaseCount): StockQuantity {
        if (canNotSubtract(decreaseCount)) {
            throw DomainException("在庫数は[$MIN]以下にできません。")
        }

        val newValue = value - decreaseCount.value
        return StockQuantity(newValue)
    }
}
