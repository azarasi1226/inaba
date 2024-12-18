package jp.inaba.catalog.api.domain.product.event

data class ProductInboundedEvent(
    override val id: String,
    val quantity: Int,
) : ProductEvent