package jp.inaba.catalog.service.presentation.product.create

data class CreateProductRequest(
    val name: String,
    val description: String,
    val imageUrl: String,
    val price: Int,
    val quantity: Int,
)