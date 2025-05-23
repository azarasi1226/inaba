package jp.inaba.message.product.event

data class ProductCreatedEvent(
    val id: String,
    val brandId: String,
    val name: String,
    val description: String,
    val imageUrl: String?,
    val price: Int,
    val quantity: Int,
)
