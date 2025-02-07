package jp.inaba.service.infrastructure.projector.order

import jp.inaba.message.order.event.OrderCompletedEvent
import jp.inaba.message.order.event.OrderFailedEvent
import jp.inaba.message.order.event.OrderIssuedEvent
import jp.inaba.service.domain.order.OrderStatus
import jp.inaba.service.infrastructure.jpa.order.OrderJpaEntity
import jp.inaba.service.infrastructure.jpa.order.OrderJpaRepository
import org.axonframework.config.ProcessingGroup
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
@ProcessingGroup(OrderProjectorEventProcessor.PROCESSOR_NAME)
class OrderProjector(
    private val repository: OrderJpaRepository,
) {
    @EventHandler
    fun on(event: OrderIssuedEvent) {
        val entity =
            OrderJpaEntity(
                id = event.id,
                status = OrderStatus.Issued,
                userId = event.userId,
            )

        repository.save(entity)
    }

    @EventHandler
    fun on(event: OrderCompletedEvent) {
        val entity = repository.findById(event.id).orElseThrow()

        entity.status = OrderStatus.Completed

        repository.save(entity)
    }

    @EventHandler
    fun on(event: OrderFailedEvent) {
        val entity = repository.findById(event.id).orElseThrow()

        entity.status = OrderStatus.Failed

        repository.save(entity)
    }
}
