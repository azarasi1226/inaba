package jp.inaba.identity.service.presentation.user.get

import jp.inaba.common.presentation.shared.ErrorResponse
import jp.inaba.identity.api.domain.user.UserErrors.FindById.USER_NOT_FOUND
import jp.inaba.identity.api.domain.user.UserId
import jp.inaba.identity.api.domain.user.UserQueries
import jp.inaba.identity.api.domain.user.findUserById
import jp.inaba.identity.service.presentation.user.UserControllerBase
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
    private val queryGateway: QueryGateway
) : UserControllerBase() {
    @GetMapping("/{userId}")
    fun handle(
        @PathVariable("userId")
        rawUserId: String,
    ): ResponseEntity<Any> {
        val userId = UserId(rawUserId)
        val query = UserQueries.FindByIdQuery(userId)

        val result = queryGateway.findUserById(query)

        return if(result.isOk) {
            ResponseEntity(
                GetUserResponse(result.value.name),
                HttpStatus.OK
            )
        } else {
            when(result.error) {
                USER_NOT_FOUND ->
                    ResponseEntity(
                        ErrorResponse(
                            errorCode = result.error.errorCode,
                            errorMessage = result.error.errorMessage
                        ),
                        HttpStatus.NOT_FOUND
                    )
            }
        }
    }
}