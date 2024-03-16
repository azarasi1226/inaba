package jp.inaba.catalog.api.domain.product

import de.huxhorn.sulky.ulid.ULID

data class ProductId(val value: String) {
    constructor() : this(ULID().nextULID())

    init {
        ULID.parseULID(value)
    }

    override fun toString(): String {
        return value
    }
}