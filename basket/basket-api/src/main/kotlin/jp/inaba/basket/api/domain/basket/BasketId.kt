package jp.inaba.basket.api.domain.basket

import de.huxhorn.sulky.ulid.ULID

data class BasketId(val value: String) {
    constructor() : this(ULID().nextULID())

    init {
        ULID.parseULID(value)
    }

    override fun toString(): String {
        return value
    }
}