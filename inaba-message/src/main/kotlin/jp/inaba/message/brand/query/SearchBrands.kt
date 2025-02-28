package jp.inaba.message.brand.query

import jp.inaba.core.domain.common.Page
import jp.inaba.core.domain.common.PagingCondition

data class SearchBrandsQuery(
    val likeBrandName: String,
    val pagingCondition: PagingCondition,
)

data class SearchBrandsResult(
    val page: Page<Summary>,
) {
    data class Summary(
        val id: String,
        val name: String,
    )
}
