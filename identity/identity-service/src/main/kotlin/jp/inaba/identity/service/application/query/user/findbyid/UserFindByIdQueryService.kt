package jp.inaba.identity.service.application.query.user.findbyid

import jp.inaba.identity.api.domain.user.FindUserByIdQuery
import jp.inaba.identity.api.domain.user.FindUserByIdResult
import jp.inaba.identity.service.infrastructure.jpa.user.UserJpaRepository
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component
import java.util.Optional

@Component
class UserFindByIdQueryService(
    private val repository: UserJpaRepository,
) {
    @QueryHandler
    fun handle(query: FindUserByIdQuery): Optional<FindUserByIdResult> {
        val maybeUser = repository.findById(query.userId.value)

        return if (maybeUser.isEmpty) {
            Optional.empty()
        } else {
            val user = maybeUser.get()
            val result =
                FindUserByIdResult(
                    id = user.id,
                    name = user.userName,
                )
            Optional.of(result)
        }
    }
}
