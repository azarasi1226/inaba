package jp.inaba.service.application.query.order

import jp.inaba.order.api.domain.order.OrderCompletedEvent
import jp.inaba.order.api.domain.order.OrderIssuedEvent
import jp.inaba.service.domain.order.OrderStatus
import jp.inaba.service.infrastructure.jpa.order.OrderEntity
import jp.inaba.service.infrastructure.jpa.order.OrderJpaRepository
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
class OrderProjection(
    private val orderJpaRepository: OrderJpaRepository
) {
    @EventHandler
    fun on(event: OrderIssuedEvent) {
        val entity = OrderEntity(
            id = event.id.value,
            status = OrderStatus.Issued,
            userId = event.userId
        )

        orderJpaRepository.save(entity)
    }

    @EventHandler
    fun on(event: OrderCompletedEvent) {
        val entity = orderJpaRepository.findById(event.id.value).orElseThrow()

        entity.status = OrderStatus.Completed

        orderJpaRepository.save(entity)
    }
}