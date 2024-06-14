package jp.inaba.basket.service.infrastructure.jpa.basket

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jp.inaba.basket.service.infrastructure.jpa.product.ProductJpaEntity

@Entity
@Table(name = "basket")
data class BasketJpaEntity(
    @EmbeddedId
    val basketItemId: BasketItemId = BasketItemId(),
    @Column(name = "basket_id", insertable = false, updatable = false)
    val basketId: String = "",
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", insertable = false, updatable = false)
    val product: ProductJpaEntity = ProductJpaEntity(),
    val itemQuantity: Int = 0,
)

@Embeddable
data class BasketItemId(
    @Column(name = "basket_id")
    val basketId: String = "",
    @Column(name = "product_id")
    val productId: String = "",
)
