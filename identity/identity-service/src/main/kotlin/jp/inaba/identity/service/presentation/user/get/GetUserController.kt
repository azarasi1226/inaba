package jp.inaba.identity.service.presentation.user.get

import com.github.michaelbull.result.mapBoth
import jp.inaba.common.presentation.shared.ErrorResponse
import jp.inaba.identity.api.domain.user.FindUserByIdError
import jp.inaba.identity.api.domain.user.FindUserByIdQuery
import jp.inaba.identity.api.domain.user.UserId
import jp.inaba.identity.api.domain.user.findUserById
import jp.inaba.identity.service.presentation.user.UserController
import org.axonframework.queryhandling.QueryGateway
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class GetUserController(
    private val queryGateway: QueryGateway,
) : UserController {
    @GetMapping("/{userId}")
    fun handle(
        @PathVariable("userId")
        rawUserId: String,
    ): ResponseEntity<Any> {
        val userId = UserId(rawUserId)
        val query = FindUserByIdQuery(userId)

        return queryGateway.findUserById(query)
            .mapBoth(
                success = {
                    ResponseEntity
                        .status(HttpStatus.OK)
                        .body(GetUserResponse(
                            name = it.name,
                        ))
                },
                failure = {
                    when (it) {
                        FindUserByIdError.USER_NOT_FOUND ->
                            ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(ErrorResponse(it))
                    }
                }
            )
    }
}
