package jp.inaba.message.order.event

data class OrderFailedEvent(
    override val id: String,
) : OrderEvent
