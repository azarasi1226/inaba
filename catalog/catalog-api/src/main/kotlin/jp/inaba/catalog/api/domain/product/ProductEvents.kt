package jp.inaba.catalog.api.domain.product

sealed interface ProductEvent {
    val id: String
}

data class ProductCreatedEvent(
    override val id: String,
    val name: String,
    val description: String,
    val imageUrl: String?,
    val price: Int,
    val quantity: Int,
) : ProductEvent

data class ProductUpdatedEvent(
    override val id: String,
    val name: String,
    val description: String,
    val imageUrl: String?,
    val price: Int,
) : ProductEvent

data class ProductShippedEvent(
    override val id: String,
    val quantity: Int,
) : ProductEvent

// TODO(過去形)
data class ProductInboundEvent(
    override val id: String,
    val quantity: Int,
) : ProductEvent

data class ProductDeletedEvent(
    override val id: String,
) : ProductEvent
