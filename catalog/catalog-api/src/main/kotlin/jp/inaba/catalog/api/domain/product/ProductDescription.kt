package jp.inaba.catalog.api.domain.product

import jp.inaba.common.domain.shared.DomainException

data class ProductDescription(val value: String) {
    companion object {
        private const val MIN_LENGTH = 1
        private const val MAX_LENGTH = 2_000
    }

    init {
        if (value.length !in MIN_LENGTH..MAX_LENGTH) {
            throw DomainException("商品説明の長さは[${MIN_LENGTH} ~ ${MAX_LENGTH}]間です。value.length:[${value.length}]")
        }
    }
}
