package jp.inaba.order.api.domain.order

interface OrderEvent {
    val id: OrderId
}

data class OrderIssuedEvent(
    override val id: OrderId,
    val userId: String,
) : OrderEvent

data class OrderCompletedEvent(override val id: OrderId) : OrderEvent

data class OrderFailedEvent(override val id: OrderId) : OrderEvent
