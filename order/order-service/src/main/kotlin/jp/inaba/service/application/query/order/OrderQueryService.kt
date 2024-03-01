package jp.inaba.service.application.query.order

import jp.inaba.service.infrastructure.jpa.order.OrderJpaRepository
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Service

@Service
class OrderQueryService(
    private val orderRepository: OrderJpaRepository
) {
    @QueryHandler
    fun handle(query: OrderFindByUserQuery) : List<OrderFindByUserResult> {
        val orders = orderRepository.findByUserId(query.userId)

        return orders.map {
            OrderFindByUserResult(
                it.id,
                it.userId,
                it.status.toString()
            )
        }
    }
}