package jp.inaba.core.domain.brand

import jp.inaba.core.domain.common.ValueObjectException

data class BrandName(val value: String) {
    companion object {
        private const val MIN = 1
        private const val MAX = 300
    }

    init {
        if (value.length !in MIN..MAX) {
            throw ValueObjectException("ブランド名の長さは[$MIN ~ $MAX]の間です。value:[$value]")
        }
    }
}
