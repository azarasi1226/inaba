package jp.inaba.service.infrastructure.jpa.product

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "product")
data class ProductJpaEntity(
    @Id
    val id: String = "",
    val brandId: String = "",
    val name: String = "",
    val description: String = "",
    val imageUrl: String? = null,
    val price: Int = 0,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
)
