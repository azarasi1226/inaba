package jp.inaba.service2.feature.query.user.findmetadata

import jp.inaba.message.user.query.FindUserMetadataBySubjectData
import jp.inaba.message.user.query.FindUserMetadataBySubjectQuery
import jp.inaba.message.user.query.FindUserMetadataBySubjectResult
import jp.inaba.service.infrastructure.jooq.generated.tables.references.USER_METADATA
import org.axonframework.messaging.queryhandling.annotation.QueryHandler
import org.jooq.DSLContext
import org.springframework.stereotype.Component

@Component
class FindUserMetadataBySubjectQueryHandler(
    private val dsl: DSLContext,
) {
    @QueryHandler
    fun handle(query: FindUserMetadataBySubjectQuery): FindUserMetadataBySubjectResult {
        val data = dsl
            .selectFrom(USER_METADATA)
            .where(USER_METADATA.SUBJECT.eq(query.subject))
            .fetchOne {
                FindUserMetadataBySubjectData(
                    userId = it.userId,
                    basketId = it.basketId!!,
                )
            }

        return if (data != null) {
            FindUserMetadataBySubjectResult.success(data)
        } else {
            FindUserMetadataBySubjectResult.notFound()
        }
    }
}
