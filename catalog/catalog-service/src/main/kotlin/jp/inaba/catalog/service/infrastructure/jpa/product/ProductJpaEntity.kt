package jp.inaba.catalog.service.infrastructure.jpa.product

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class ProductJpaEntity(
    @Id
    val productId: String = "",
    val productName: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val price: Int = 0,
    val quantity: Int = 0,
)