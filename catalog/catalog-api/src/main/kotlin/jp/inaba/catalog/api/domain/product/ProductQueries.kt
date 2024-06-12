package jp.inaba.catalog.api.domain.product

data class FindProductById(
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