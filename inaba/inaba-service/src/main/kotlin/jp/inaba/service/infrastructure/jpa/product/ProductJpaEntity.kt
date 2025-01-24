package jp.inaba.service.infrastructure.jpa.product

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "product")
data class ProductJpaEntity(
    @Id
    val id: String = "",
    val stockId: String? = null,
    val name: String = "",
    val description: String = "",
    val imageUrl: String? = null,
    val price: Int = 0,
    val quantity: Int = 0,
)
