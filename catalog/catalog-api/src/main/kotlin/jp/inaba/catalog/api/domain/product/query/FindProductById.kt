package jp.inaba.catalog.api.domain.product.query

import jp.inaba.catalog.share.domain.product.ProductId

data class FindProductByIdQuery(
    val id: ProductId,
)

data class FindProductByIdResult(
    val id: String,
    val name: String,
    val description: String,
    val imageUrl: String?,
    val price: Int,
    val quantity: Int,
)
