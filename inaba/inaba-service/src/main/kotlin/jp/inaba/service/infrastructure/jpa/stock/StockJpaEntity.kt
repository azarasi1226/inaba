package jp.inaba.service.infrastructure.jpa.stock

import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "stock")
data class StockJpaEntity(
    val id: String,
    val productId: String,
    val quantity: Int,
)