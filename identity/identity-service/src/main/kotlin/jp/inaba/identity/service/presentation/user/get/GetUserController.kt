package jp.inaba.identity.service.presentation.user.get

import jp.inaba.identity.api.domain.user.UserId
import jp.inaba.identity.api.domain.user.UserQueries
import org.axonframework.extensions.kotlin.queryOptional
import org.axonframework.queryhandling.QueryGateway
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class GetUserController(
    private val queryGateway: QueryGateway
) {
    @GetMapping("/{userId}")
    fun get(
        @PathVariable("userId")
        rawUserId: String,
    ): GetUserResponse {
        val userId = UserId(rawUserId)
        val query = UserQueries.FindByIdQuery(userId)
        val result = queryGateway.queryOptional<UserQueries.FindByIdResult, UserQueries.FindByIdQuery>(query)
            .get()
            .orElseThrow { UserNotFoundException(userId) }

        return GetUserResponse(result.name)
    }
}