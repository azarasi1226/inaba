package jp.inaba.core.domain.product

import jp.inaba.core.domain.common.DomainException

data class ProductDescription(val value: String) {
    companion object {
        private const val MIN_LENGTH = 1
        private const val MAX_LENGTH = 2_000
    }

    init {
        if (value.length !in MIN_LENGTH..MAX_LENGTH) {
            throw DomainException("商品説明の長さは[$MIN_LENGTH ~ $MAX_LENGTH]間です。length:[${value.length}]")
        }
    }
}
