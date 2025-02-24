package jp.inaba.core.domain.basket

import jp.inaba.core.domain.common.ValueObjectException

data class BasketItemQuantity(val value: Int) {
    companion object {
        private const val MIN = 1
        private const val MAX = 99
    }

    init {
        if (value !in MIN..MAX) {
            throw ValueObjectException("買い物かごアイテムの許容個数は[$MIN ~ $MAX]の間です。value:[$value]")
        }
    }
}
