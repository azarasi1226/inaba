package jp.inaba.catalog.api.domain.product.event

data class ProductDeletedEvent(
    override val id: String,
) : ProductEvent
