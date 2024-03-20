package jp.inaba.basket.service.infrastructure.jpa.basket

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "basket")
data class BasketJpaEntity(
    @Id
    var id: String = "",
    @Column(unique = true)
    var userId: String = "",
)