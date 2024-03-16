package jp.inaba.catalog.api.domain.product

sealed interface ProductEvent {
    val id: ProductId
}

data class ProductCreatedEvent(
    override val id: ProductId,
    val productName: String,
    val description: String,
    val imageUrl: String,
    val price: Int,
    val quantity: Int
) : ProductEvent

data class ProductUpdatedEvent(
    override val id: ProductId,
    val productName: String,
    val description: String,
    val imageUrl: String,
    val price: Int,
    val quantity: Int
) : ProductEvent

data class ProductDeletedEvent(
    override val id: ProductId
) : ProductEvent