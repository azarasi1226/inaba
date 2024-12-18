package jp.inaba.catalog.service.presentation.product.get

import jp.inaba.catalog.api.domain.product.query.FindProductByIdResult

data class GetProductResponse(
    val name: String,
    val description: String,
    val imageUrl: String?,
    val price: Int,
    val quantity: Int,
) {
    companion object {
        fun create(result: FindProductByIdResult): GetProductResponse {
            return GetProductResponse(
                name = result.name,
                description = result.description,
                imageUrl = result.imageUrl,
                price = result.price,
                quantity = result.quantity
            )
        }
    }
}
