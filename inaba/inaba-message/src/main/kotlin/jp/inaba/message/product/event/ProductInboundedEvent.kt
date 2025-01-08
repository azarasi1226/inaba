package jp.inaba.message.product.event

data class ProductInboundedEvent(
    override val id: String,
    val quantity: Int,
) : ProductEvent
