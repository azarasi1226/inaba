package jp.inaba.catalog.api.domain.product.event

data class ProductShippedEvent(
    override val id: String,
    val quantity: Int,
) : ProductEvent