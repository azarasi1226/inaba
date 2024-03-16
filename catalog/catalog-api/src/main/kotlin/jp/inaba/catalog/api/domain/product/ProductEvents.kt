package jp.inaba.catalog.api.domain.product

sealed interface ProductEvent {
    val id: ProductId
}

object ProductEvents {
    data class Created(
        override val id: ProductId,
        val name: String,
        val description: String,
        val imageUrl: String,
        val price: Int,
        val quantity: Int
    ) : ProductEvent

    data class Updated(
        override val id: ProductId,
        val name: String,
        val description: String,
        val imageUrl: String,
        val price: Int,
        val quantity: Int
    ) : ProductEvent

    data class Deleted(
        override val id: ProductId
    ) : ProductEvent
}

