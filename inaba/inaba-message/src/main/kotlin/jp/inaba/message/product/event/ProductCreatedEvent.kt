package jp.inaba.message.product.event

data class ProductCreatedEvent(
    override val id: String,
    val brandId: String,
    val name: String,
    val description: String,
    val imageUrl: String?,
    val price: Int,
) : ProductEvent
