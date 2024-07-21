package jp.inaba.datakey.service.jp.inaba.datakey.service.infrastructure.jpa.datakey

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Lob
import jakarta.persistence.Table

@Entity
@Table(name = "datakeys")
data class DataKeyJpaEntity(
    @Id
    val id: String = "",
    @Lob
    val encryptedDataKey: ByteArray = byteArrayOf(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DataKeyJpaEntity

        if (id != other.id) return false
        if (!encryptedDataKey.contentEquals(other.encryptedDataKey)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + encryptedDataKey.contentHashCode()
        return result
    }
}
