package jp.inaba.service.application.query.user

import jp.inaba.core.domain.common.UseCaseException
import jp.inaba.core.domain.user.FindUserMetadataBySubjectError
import jp.inaba.message.user.query.FindUserMetadataBySubjectQuery
import jp.inaba.message.user.query.FindUserMetadataBySubjectResult
import jp.inaba.service.infrastructure.jpa.usermetadata.UserMetadataJpaRepository
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class FindUserMetadataBySubjectQueryService(
    private val repository: UserMetadataJpaRepository,
) {
    @QueryHandler
    fun handle(query: FindUserMetadataBySubjectQuery): FindUserMetadataBySubjectResult {
        val entity =
            repository.findById(query.subject).orElseThrow {
                UseCaseException(FindUserMetadataBySubjectError.USER_METADATA_NOT_FOUND)
            }

        return FindUserMetadataBySubjectResult(
            userId = entity.userId,
            basketId = entity.basketId,
        )
    }
}
