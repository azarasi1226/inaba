package jp.inaba.basket.service.infrastructure.jpa.lookupbasket

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "lookup_basket")
data class LookupBasketJpaEntity(
    @Id
    val basketId: String = "",
    val userId: String = "",
)
