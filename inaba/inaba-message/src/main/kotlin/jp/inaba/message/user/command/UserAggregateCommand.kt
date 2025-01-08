package jp.inaba.message.user.command

import jp.inaba.core.domain.user.UserId
import org.axonframework.modelling.command.TargetAggregateIdentifier

interface UserAggregateCommand {
    @get:TargetAggregateIdentifier
    val id: UserId
}
