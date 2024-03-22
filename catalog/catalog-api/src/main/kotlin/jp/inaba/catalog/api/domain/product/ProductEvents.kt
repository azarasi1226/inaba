package jp.inaba.catalog.api.domain.product

sealed interface ProductEvent {
    val id: ProductId
}

object ProductEvents {
    data class Created(
        override val id: ProductId,
        val name: ProductName,
        val description: ProductDescription,
        val imageUrl: ProductImageURL,
        val price: ProductPrice,
        val quantity: ProductQuantity
    ) : ProductEvent

    data class Updated(
        override val id: ProductId,
        val name: ProductName,
        val description: ProductDescription,
        val imageUrl: ProductImageURL,
        val price: ProductPrice,
        val quantity: ProductQuantity
    ) : ProductEvent

    data class Deleted(
        override val id: ProductId
    ) : ProductEvent
}

