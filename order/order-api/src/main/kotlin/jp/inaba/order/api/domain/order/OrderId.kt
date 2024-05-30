package jp.inaba.order.api.domain.order

import de.huxhorn.sulky.ulid.ULID

data class OrderId(val value: String) {
    constructor() : this(ULID().nextULID())

    init {
        try {
            ULID.parseULID(value)
        } catch (e: Exception) {
            throw e
        }
    }

    override fun toString(): String {
        return value
    }
}
