package jp.inaba.catalog.service.presentation.product.update

data class UpdateProductRequest(
    val name: String,
    val description: String,
    val imageUrl: String,
    val price: Int,
)