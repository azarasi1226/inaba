package jp.inaba.service.application.query.brand

import jp.inaba.core.domain.brand.FindBrandByIdError
import jp.inaba.core.domain.common.UseCaseException
import jp.inaba.message.brand.query.FindBrandByIdQuery
import jp.inaba.message.brand.query.FindBrandByIdResult
import jp.inaba.service.infrastructure.jooq.generated.tables.references.BRAND
import org.axonframework.queryhandling.QueryHandler
import org.jooq.DSLContext
import org.springframework.stereotype.Component

@Component
class FindBrandByIdQueryService(
    private val dsl: DSLContext,
) {
    @QueryHandler
    fun handle(query: FindBrandByIdQuery): FindBrandByIdResult =
        dsl
            .selectFrom(BRAND)
            .where(BRAND.ID.eq(query.id.value))
            .fetchOne {
                FindBrandByIdResult(
                    name = it.name!!,
                )
            } ?: throw UseCaseException(FindBrandByIdError.BRAND_NOD_FOUND)
}
