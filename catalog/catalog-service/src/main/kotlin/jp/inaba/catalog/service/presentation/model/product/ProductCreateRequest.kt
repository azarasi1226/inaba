package jp.inaba.catalog.service.presentation.model.product

data class ProductCreateRequest(
    val name: String,
    val description: String,
    val imageUrl: String,
    val price: Int,
    val quantity: Int
)
