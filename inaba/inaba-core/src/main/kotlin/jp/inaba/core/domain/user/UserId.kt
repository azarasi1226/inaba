package jp.inaba.core.domain.user

import de.huxhorn.sulky.ulid.ULID
import jp.inaba.core.domain.common.DomainException

data class UserId(val value: String) {
    constructor() : this(ULID().nextULID())

    init {
        try {
            ULID.parseULID(value)
        } catch (ex: Exception) {
            throw DomainException("UserIdはULIDの形式である必要があります。value:[$value]")
        }
    }

    override fun toString(): String {
        return value
    }
}
