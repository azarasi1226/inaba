package jp.inaba.basket.api.domain.basket

import jp.inaba.common.domain.shared.DomainException

data class BasketItemQuantity(val value: Int) {
    companion object {
        private const val MIN = 1
        private const val MAX = 99
    }

    init {
        if(value !in MIN..MAX) {
            throw DomainException("買い物かごアイテムの許容個数は[${MIN} ~ ${MAX}]の間です。現在の個数[${value}]")
        }
    }
}