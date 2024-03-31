package jp.inaba.identity.service.presentation.user.create

import jp.inaba.identity.api.domain.user.UserCommands
import jp.inaba.identity.api.domain.user.UserId
import org.axonframework.commandhandling.gateway.CommandGateway
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


// 本当は使わないよこれ、検証のために仕方なくね
@RestController
@RequestMapping("/api/users")
class CreateUserController(
    private val commandGateway: CommandGateway
) {
    @PostMapping
    fun create(
        @RequestBody
        request: CreateUserRequest
    ): CreateUserResponse {
        val userId = UserId()
        val command = UserCommands.Create(userId)

        commandGateway.sendAndWait<Any>(command)

        return CreateUserResponse(userId.value)
    }
}