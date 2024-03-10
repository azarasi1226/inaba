package jp.inaba.basket.service.infrastructure.jpa.basket

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class BasketJpaEntity(
    @Id
    var basketId: String = "",
    @Column(unique = true)
    var userId: String = "",
)