package jp.inaba.identity.service.application.query.user.findbyid

import jp.inaba.identity.api.domain.user.UserQueries
import jp.inaba.identity.service.infrastructure.jpa.user.UserJpaRepository
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserFindByIdQueryService(
    private val repository: UserJpaRepository
) {
    @QueryHandler
    fun handle(query: UserQueries.FindByIdQuery): Optional<UserQueries.FindByIdResult> {
        val maybeUser = repository.findById(query.userId.value)

        return if(maybeUser.isEmpty) {
            Optional.empty()
        } else {
            val user = maybeUser.get()
            val result = UserQueries.FindByIdResult(
                id = user.id,
                name = user.userName
            )
            Optional.of(result)
        }
    }
}