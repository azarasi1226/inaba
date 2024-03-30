package jp.inaba.catalog.api.domain.product

import de.huxhorn.sulky.ulid.ULID
import jp.inaba.common.domain.shared.ValueObjectException

data class ProductId(val value: String) {
    constructor() : this(ULID().nextULID())

    init {
        try {
            ULID.parseULID(value)
        }
        catch (ex: Exception){
            throw ValueObjectException("ProductIdはULIDの形式である必要があります。現在のID[${value}]")
        }
    }

    override fun toString(): String {
        return value
    }
}