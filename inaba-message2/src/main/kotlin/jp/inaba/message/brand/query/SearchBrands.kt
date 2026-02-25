package jp.inaba.message.brand.query

import jp.inaba.core.domain.common.Page
import jp.inaba.core.domain.common.PagingCondition
import jp.inaba.message.Error
import jp.inaba.message.QueryResult

data class SearchBrandsQuery(
    val likeBrandName: String,
    val pagingCondition: PagingCondition,
)

data class SearchBrandsResult private constructor(
    override val success: Boolean,
    override val data: Page<SearchBrandsSummary>?,
    override val error: Error?,
) : QueryResult<Page<SearchBrandsSummary>> {
    companion object {
        fun success(page: Page<SearchBrandsSummary>): SearchBrandsResult =
            SearchBrandsResult(true, page, null)
    }
}

data class SearchBrandsSummary(
    val id: String,
    val name: String,
)
