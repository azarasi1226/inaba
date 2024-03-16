package jp.inaba.catalog.service.presentation.model.product

data class CreateProductRequest(
    val productName: String,
    val description: String,
    val imageUrl: String,
    val price: Int,
    val quantity: Int
)