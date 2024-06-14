package jp.inaba.identity.api.domain.user

import org.axonframework.modelling.command.TargetAggregateIdentifier

interface UserAggregateCommand {
    @get:TargetAggregateIdentifier
    val id: UserId
}

data class CreateUserCommand(
    override val id: UserId,
) : UserAggregateCommand

data class DeleteUserCommand(
    override val id: UserId,
) : UserAggregateCommand