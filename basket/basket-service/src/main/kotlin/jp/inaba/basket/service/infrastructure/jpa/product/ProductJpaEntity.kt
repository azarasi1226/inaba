package jp.inaba.basket.service.infrastructure.jpa.product

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "product")
data class ProductJpaEntity(
    @Id
    val id: String = "",
    val name: String = "",
    val imageUrl: String? = null,
    val price: Int = 0,
)
