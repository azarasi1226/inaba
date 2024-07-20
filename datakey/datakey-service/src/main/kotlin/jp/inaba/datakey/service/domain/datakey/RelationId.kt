package jp.inaba.datakey.service.jp.inaba.datakey.service.domain.datakey

import de.huxhorn.sulky.ulid.ULID
import jp.inaba.common.domain.shared.DomainException

data class RelationId(val value: String) {
    init {
        try {
            ULID.parseULID(value)
        } catch (ex: Exception) {
            throw DomainException("RelationIdはULIDの形式である必要があります。value:[$value]")
        }
    }

    override fun toString(): String {
        return value
    }
}
