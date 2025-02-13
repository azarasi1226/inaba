package jp.inaba.service.infrastructure.jpa.lookupstock

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "lookup_stock")
data class LookupStockJpaEntity(
    @Id
    val id: String = "",
    @Column(unique = true)
    val productId: String = "",
)
