package jp.inaba.message.order.event

data class OrderCompletedEvent(
    override val id: String,
) : OrderEvent