package jp.inaba.service.infrastructure.projector.order

import jp.inaba.message.order.event.OrderCompletedEvent
import jp.inaba.message.order.event.OrderIssuedEvent
import jp.inaba.service.domain.order.OrderStatus
import jp.inaba.service.infrastructure.jpa.order.OrderEntity
import jp.inaba.service.infrastructure.jpa.order.OrderJpaRepository
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
@ProcessingGroup(OrderProjectorEventProcessor.PROCESSOR_NAME)
class OrderProjector(
    private val orderJpaRepository: OrderJpaRepository,
) {
    @EventHandler
    fun on(event: OrderIssuedEvent) {
        val entity =
            OrderEntity(
                id = event.id,
                status = OrderStatus.Issued,
                userId = event.userId,
            )

        orderJpaRepository.save(entity)
    }

    @EventHandler
    fun on(event: OrderCompletedEvent) {
        val entity = orderJpaRepository.findById(event.id).orElseThrow()

        entity.status = OrderStatus.Completed

        orderJpaRepository.save(entity)
    }
}
