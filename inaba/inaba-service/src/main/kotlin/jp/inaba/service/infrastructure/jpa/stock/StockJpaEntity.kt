package jp.inaba.service.infrastructure.jpa.stock

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jp.inaba.service.infrastructure.jpa.product.ProductJpaEntity

@Entity
@Table(name = "stock")
data class StockJpaEntity(
    @Id
    val id: String = "",
    @ManyToOne
    @JoinColumn(name = "product_id")
    val product: ProductJpaEntity = ProductJpaEntity(),
    val quantity: Int = 0,
)
