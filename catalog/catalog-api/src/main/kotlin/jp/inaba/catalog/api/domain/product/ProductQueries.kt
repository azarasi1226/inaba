package jp.inaba.catalog.api.domain.product

object ProductQueries {
    data class FindById(
        val id: ProductId
    )

    data class FindByIdResult(
        val id: String,
        val name: String,
        val description: String,
        val imageUrl: String,
        val price: Int,
        val quantity: Int
    )
}