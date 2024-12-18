package jp.inaba.catalog.share.domain.product

import jp.inaba.common.domain.shared.DomainException

data class ProductName(val value: String) {
    companion object {
        private const val MIN_LENGTH = 1
        private const val MAX_LENGTH = 200
    }

    init {
        if (value.length !in MIN_LENGTH..MAX_LENGTH) {
            throw DomainException("商品名の長さは[$MIN_LENGTH~$MAX_LENGTH]の間です。value.length[${value.length}]")
        }
    }
}
