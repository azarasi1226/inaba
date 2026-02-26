package jp.inaba.service2.features.query.brand.findbyid

import jp.inaba.message.brand.query.FindBrandByIdData
import jp.inaba.message.brand.query.FindBrandByIdQuery
import jp.inaba.message.brand.query.FindBrandByIdResult
import jp.inaba.service.infrastructure.jooq.generated.tables.references.BRANDS
import org.axonframework.messaging.queryhandling.annotation.QueryHandler
import org.jooq.DSLContext
import org.springframework.stereotype.Component

@Component
class FindBrandByIdQueryHandler(
  private val dsl: DSLContext,
) {
  @QueryHandler
  fun handle(query: FindBrandByIdQuery): FindBrandByIdResult {
    val data = dsl
      .selectFrom(BRANDS)
      .where(BRANDS.ID.eq(query.id.value))
      .fetchOne {
        FindBrandByIdData(
          id = it.id,
          name = it.name,
        )
      }

    return if (data != null) {
      FindBrandByIdResult.success(data)
    } else {
      FindBrandByIdResult.notFound()
    }
  }
}
