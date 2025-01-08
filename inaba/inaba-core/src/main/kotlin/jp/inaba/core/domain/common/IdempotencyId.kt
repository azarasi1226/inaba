package jp.inaba.core.domain.common

import de.huxhorn.sulky.ulid.ULID

data class IdempotencyId(val value: String) {
    constructor() : this(ULID().nextULID())

    init {
        try {
            ULID.parseULID(value)
        } catch (ex: Exception) {
            throw DomainException("${this::class.simpleName}はULIDの形式である必要があります。value:[$value]")
        }
    }

    override fun toString(): String {
        return value
    }
}
