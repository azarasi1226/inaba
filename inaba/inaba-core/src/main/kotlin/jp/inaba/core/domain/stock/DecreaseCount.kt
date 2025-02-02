package jp.inaba.core.domain.stock

import jp.inaba.core.domain.common.DomainException

data class DecreaseCount(val value: Int) {
    companion object {
        private const val MIN = 1
        private const val MAX = 1_000_000
    }

    init {
        if (value !in MIN .. MAX) {
            throw DomainException("減少値は[${MIN}~${MAX}]の間です。value:[$value]")
        }
    }
}