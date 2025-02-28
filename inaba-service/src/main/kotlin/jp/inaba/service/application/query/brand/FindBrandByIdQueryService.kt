package jp.inaba.service.application.query.brand

import jp.inaba.core.domain.brand.FindBrandByIdError
import jp.inaba.core.domain.common.UseCaseException
import jp.inaba.message.brand.query.FindBrandByIdQuery
import jp.inaba.message.brand.query.FindBrandByIdResult
import jp.inaba.service.infrastructure.jpa.brand.BrandJpaRepository
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class FindBrandByIdQueryService(
    private val repository: BrandJpaRepository,
) {
    @QueryHandler
    fun handle(query: FindBrandByIdQuery): FindBrandByIdResult {
        val entity =
            repository.findById(query.id.value).orElseThrow {
                UseCaseException(FindBrandByIdError.BRAND_NOD_FOUND)
            }

        return FindBrandByIdResult(
            name = entity.name,
        )
    }
}
