package jp.inaba.service.infrastructure.jpa.lookupproduct

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "lookup_product")
class LookupProductJpaEntity(
    @Id
    val id: String = "",
    val name: String = "",
)