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

    fun canAdd(amount: StockQuantity): Boolean {
        val newValue = value + amount.value

        return newValue < MAX
    }

    fun add(amount: StockQuantity): StockQuantity {
        if (canAdd(amount)) {
            throw DomainException("在庫数の上限は[$MAX]です。")
        }

        val newValue = value + amount.value
        return StockQuantity(newValue)
    }

    fun canSubtract(amount: StockQuantity): Boolean {
        val newValue = value - amount.value

        return newValue > MIN
    }

    fun subtract(amount: StockQuantity): StockQuantity {
        if (canSubtract(amount)) {
            throw DomainException("在庫数は[$MIN]以下にできません。")
        }

        val newValue = value - amount.value
        return StockQuantity(newValue)
    }
}
