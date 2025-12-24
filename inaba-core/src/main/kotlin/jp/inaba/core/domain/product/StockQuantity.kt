package jp.inaba.core.domain.product

import jp.inaba.core.domain.common.ValueObjectException

data class StockQuantity(
    val value: Int,
) {
    companion object {
        private const val MIN = 0
        private const val MAX = 1_000_000
    }

    init {
        if (value !in MIN..MAX) {
            throw ValueObjectException("在庫数は[$MIN~$MAX]の間です。value:[$value]")
        }
    }

    fun canNotAdd(increaseStock: IncreaseStockQuantity): Boolean {
        val newValue = value + increaseStock.value

        return newValue > MAX
    }

    fun add(increaseStock: IncreaseStockQuantity): StockQuantity {
        if (canNotAdd(increaseStock)) {
            throw ValueObjectException("在庫数の上限は[$MAX]です。")
        }

        val newValue = value + increaseStock.value
        return StockQuantity(newValue)
    }

    fun canNotSubtract(decreaseStock: DecreaseStockQuantity): Boolean {
        val newValue = value - decreaseStock.value

        return newValue < MIN
    }

    fun subtract(decreaseStock: DecreaseStockQuantity): StockQuantity {
        if (canNotSubtract(decreaseStock)) {
            throw ValueObjectException("在庫数は[$MIN]以下にできません。")
        }

        val newValue = value - decreaseStock.value
        return StockQuantity(newValue)
    }
}
