package jp.inaba.service.infrastructure.jpa.usermetadata

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table

@Entity
@Table(
    name = "user_metadata",
    indexes = [Index(columnList = "user_Id", unique = true)],
)
data class UserMetadataJpaEntity(
    @Id
    val subject: String = "",
    val userId: String = "",
    val basketId: String = "",
)
