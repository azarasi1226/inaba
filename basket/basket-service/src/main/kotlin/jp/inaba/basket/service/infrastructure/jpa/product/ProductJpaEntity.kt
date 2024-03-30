package jp.inaba.basket.service.infrastructure.jpa.product

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "product")
data class ProductJpaEntity(
    @Id
    var id: String = "",
    var name: String = "",
    var imageUrl: String? = null,
    var price: Int = 0
)