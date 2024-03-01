package jp.inaba.service.infrastructure.jpa.order

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jp.inaba.service.domain.order.OrderStatus

@Entity
data class OrderEntity(
    @Id
    var id: String = "",
    var status: OrderStatus = OrderStatus.Issued,
    var userId: String = ""
)