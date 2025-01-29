package jp.inaba.service.application.query.product

data class SearchProductSqlResult(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val price: Int,
    val quantity: Int,
    val totalCount: Long,
)
