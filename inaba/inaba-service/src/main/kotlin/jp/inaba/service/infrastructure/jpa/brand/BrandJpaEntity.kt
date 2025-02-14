package jp.inaba.service.infrastructure.jpa.brand

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "brand")
data class BrandJpaEntity(
    @Id
    val id: String = "",
    val name: String = ""
)