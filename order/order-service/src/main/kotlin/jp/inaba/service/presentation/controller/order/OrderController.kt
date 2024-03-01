package jp.inaba.service.presentation.controller.order

import jp.inaba.service.application.query.order.OrderFindByUserQuery
import jp.inaba.service.application.query.order.OrderFindByUserResult
import jp.inaba.service.presentation.model.order.FindOrderResponse
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.extensions.kotlin.queryMany
import org.axonframework.queryhandling.QueryGateway
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/orders")
class OrderController(
    private val commandGateway: CommandGateway,
    private val queryGateway: QueryGateway
) {
    @PostMapping
    fun issue() {

    }

    @GetMapping
    fun find(
        @RequestParam
        userId: String
    ): List<FindOrderResponse> {
        val query = OrderFindByUserQuery(userId)
        val result = queryGateway.queryMany<OrderFindByUserResult, OrderFindByUserQuery>(query).get()

        return result.map {
            FindOrderResponse(
                orderId = it.orderId,
                userId = it.userId,
                status = it.orderStatus
            )
        }
    }
}