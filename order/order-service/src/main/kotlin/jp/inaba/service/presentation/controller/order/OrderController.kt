package jp.inaba.service.presentation.controller.order

import jp.inaba.order.api.domain.order.IssueOrderCommand
import jp.inaba.order.api.domain.order.OrderId
import jp.inaba.service.application.query.order.OrderFindByUserIdQuery
import jp.inaba.service.application.query.order.OrderFindByUserIdResult
import jp.inaba.service.presentation.model.order.OrderFindByUserIdResponse
import jp.inaba.service.presentation.model.order.OrderIssueRequest
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.extensions.kotlin.queryMany
import org.axonframework.queryhandling.QueryGateway
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/orders")
class OrderController(
    private val commandGateway: CommandGateway,
    private val queryGateway: QueryGateway,
) {
    @PostMapping
    fun issue(
        @RequestBody
        request: OrderIssueRequest,
    ) {
        val orderId = OrderId()
        val command =
            IssueOrderCommand(
                id = orderId,
                userId = request.userId,
                productId = request.productId,
            )

        commandGateway.sendAndWait<Any>(command)
    }

    @GetMapping
    fun findByUserId(
        @RequestParam
        userId: String,
    ): List<OrderFindByUserIdResponse> {
        val query = OrderFindByUserIdQuery(userId)
        val result = queryGateway.queryMany<OrderFindByUserIdResult, OrderFindByUserIdQuery>(query).get()

        return result.map {
            OrderFindByUserIdResponse(
                orderId = it.orderId,
                userId = it.userId,
                status = it.orderStatus,
            )
        }
    }
}
