package jp.inaba.core.domain.product

import jp.inaba.core.domain.common.ValueObjectException

data class ProductName(val value: String) {
    companion object {
        private const val MIN_LENGTH = 1
        private const val MAX_LENGTH = 200
    }

    init {
        if (value.length !in MIN_LENGTH..MAX_LENGTH) {
            throw ValueObjectException("商品名の長さは[$MIN_LENGTH~$MAX_LENGTH]の間です。length[${value.length}]")
        }
    }
}
