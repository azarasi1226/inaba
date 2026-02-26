package jp.inaba.message.brand.query

import jp.inaba.core.domain.common.Page
import jp.inaba.core.domain.common.PagingCondition
import jp.inaba.message.Error
import jp.inaba.message.QueryResult
import jp.inaba.message.QueryResult2

data class SearchBrandsQuery(
    val likeBrandName: String,
    val pagingCondition: PagingCondition,
)

data class SearchBrandsSummary(
    val id: String,
    val name: String,
)

data class SearchBrandsResult private constructor(
    override val success: Boolean,
    override val data: Page<SearchBrandsSummary>?,
    override val error: Error?,
) : QueryResult2<Page<SearchBrandsSummary>> {
    companion object {
        fun success(data: Page<SearchBrandsSummary>): SearchBrandsResult =
            SearchBrandsResult(true, data, null)
    }
}
