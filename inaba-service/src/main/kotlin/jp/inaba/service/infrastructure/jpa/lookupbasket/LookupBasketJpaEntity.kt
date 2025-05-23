package jp.inaba.service.infrastructure.jpa.lookupbasket

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "lookup_basket")
data class LookupBasketJpaEntity(
    @Id
    val id: String = "",
    @Column(unique = true)
    val userId: String = "",
)
