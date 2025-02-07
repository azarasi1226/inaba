package jp.inaba.service.infrastructure.jpa.lookupuser

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "lookup_user")
data class LookupUserJpaEntity(
    @Id
    val id: String = "",
)
