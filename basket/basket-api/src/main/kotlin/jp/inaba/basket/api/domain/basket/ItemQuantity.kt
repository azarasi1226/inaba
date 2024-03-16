package jp.inaba.basket.api.domain.basket

import de.huxhorn.sulky.ulid.ULID

data class ItemQuantity(val value: Int) {
    companion object {
        private const val MAX_QUANTITY = 99
    }

    init {
        if(value !in 1..MAX_QUANTITY) {
            throw IllegalArgumentException("最大数量は${MAX_QUANTITY}です。現在の個数:${value}")
        }
    }
}