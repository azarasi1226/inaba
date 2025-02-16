package jp.inaba.service.infrastructure.jpa.basket

import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Index
import jakarta.persistence.Table

@Entity
@Table(
    name = "basket",
    indexes = [Index(columnList = "product_id")],
)
data class BasketJpaEntity(
    @EmbeddedId
    val basketItemId: BasketItemId = BasketItemId(),
    val itemQuantity: Int = 0,
)

@Embeddable
data class BasketItemId(
    val basketId: String = "",
    val productId: String = "",
)
