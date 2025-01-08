package jp.inaba.message.product.event

data class ProductDeletedEvent(
    override val id: String,
) : ProductEvent
