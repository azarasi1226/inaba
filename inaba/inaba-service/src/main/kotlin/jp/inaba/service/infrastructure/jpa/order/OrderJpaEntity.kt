package jp.inaba.service.infrastructure.jpa.order

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jp.inaba.service.domain.order.OrderStatus

@Entity
data class OrderJpaEntity(
    @Id
    val id: String = "",
    val status: OrderStatus = OrderStatus.Issued,
    val userId: String = "",
)
