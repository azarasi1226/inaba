package jp.inaba.identity.api.domain.user

import de.huxhorn.sulky.ulid.ULID

data class UserId(val value: String) {
    constructor() : this(ULID().nextULID())

    init {
        ULID.parseULID(value)
    }

    override fun toString(): String {
        return value
    }
}