package jp.inaba.service.application.query.user

import jp.inaba.service.infrastructure.jpa.user.UserJpaRepository
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component
import java.util.Optional
import jp.inaba.message.user.query.FindUserByIdQuery
import jp.inaba.message.user.query.FindUserByIdResult

@Component
class FindUserByIdQueryService(
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
