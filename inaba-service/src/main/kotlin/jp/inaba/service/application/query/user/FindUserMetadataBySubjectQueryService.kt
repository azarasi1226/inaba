package jp.inaba.service.application.query.user

import jp.inaba.core.domain.common.UseCaseException
import jp.inaba.core.domain.user.FindUserMetadataBySubjectError
import jp.inaba.message.user.query.FindUserMetadataBySubjectQuery
import jp.inaba.message.user.query.FindUserMetadataBySubjectResult
import jp.inaba.service.infrastructure.jooq.generated.tables.references.USER_METADATA
import jp.inaba.service.infrastructure.jpa.usermetadata.UserMetadataJpaRepository
import org.axonframework.queryhandling.QueryHandler
import org.jooq.DSLContext
import org.springframework.stereotype.Component

@Component
class FindUserMetadataBySubjectQueryService(
    private val repository: UserMetadataJpaRepository,
    private val dsl: DSLContext,
) {
    @QueryHandler
    fun handle(query: FindUserMetadataBySubjectQuery): FindUserMetadataBySubjectResult =
        dsl
            .selectFrom(USER_METADATA)
            .where(USER_METADATA.SUBJECT.eq(query.subject))
            .fetchOne {
                FindUserMetadataBySubjectResult(
                    userId = it.userId,
                    // basketIDはCreateUserSagaが完了していれば必ず作成されているはずなので!!で良い
                    basketId = it.basketId!!,
                )
            } ?: throw UseCaseException(FindUserMetadataBySubjectError.USER_METADATA_NOT_FOUND)
}
