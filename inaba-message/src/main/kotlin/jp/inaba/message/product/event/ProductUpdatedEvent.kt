package jp.inaba.message.product.event

data class ProductUpdatedEvent(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String?,
    val price: Int,
)
