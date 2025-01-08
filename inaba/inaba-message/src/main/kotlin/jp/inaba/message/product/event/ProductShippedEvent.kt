package jp.inaba.message.product.event

data class ProductShippedEvent(
    override val id: String,
    val quantity: Int,
) : ProductEvent
