package jp.inaba.service.infrastructure.jpa.lookupbrand

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "lookup_brand")
data class LookupBrandJpaEntity(
    @Id
    val id: String = "",
)