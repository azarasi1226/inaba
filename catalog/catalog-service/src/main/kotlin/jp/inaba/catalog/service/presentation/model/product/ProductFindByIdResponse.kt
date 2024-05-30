package jp.inaba.catalog.service.presentation.model.product

data class ProductFindByIdResponse(
    val name: String,
    val description: String,
    val imageUrl: String?,
    val price: Int,
    val quantity: Int,
)
