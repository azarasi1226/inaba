package jp.inaba.service.infrastructure.jpa.lookupproduct

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "lookup_product")
data class LookupProductJpaEntity(
    @Id
    val id: String = "",
    val name: String = "",
)