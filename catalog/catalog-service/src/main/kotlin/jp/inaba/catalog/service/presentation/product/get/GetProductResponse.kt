package jp.inaba.catalog.service.presentation.product.get

data class GetProductResponse(
    val name: String,
    val description: String,
    val imageUrl: String?,
    val price: Int,
    val quantity: Int,
)
