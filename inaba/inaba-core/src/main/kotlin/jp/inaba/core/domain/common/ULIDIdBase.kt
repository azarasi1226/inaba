package jp.inaba.core.domain.common

import de.huxhorn.sulky.ulid.ULID

abstract class ULIDIdBase(val value: String) {
    constructor() : this(ULID().nextULID())

    init {
        try {
            ULID.parseULID(value)
        } catch (_: Exception) {
            throw DomainException("${this::class.simpleName}はULIDの形式である必要があります。value:[$value]")
        }
    }

    override fun toString(): String {
        return value
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ULIDIdBase) return false
        return value == other.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}