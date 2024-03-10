package jp.inaba.basket.service.infrastructure.jpa.item

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class ItemJpaEntity(
    @Id
    val itemId: String = "",
    val itemName: String = "",
    val itemPrice: Int = 0,
)