package jp.inaba.identity.api.domain.user.command

import jp.inaba.identity.share.domain.user.UserId
import org.axonframework.modelling.command.TargetAggregateIdentifier

interface UserAggregateCommand {
    @get:TargetAggregateIdentifier
    val id: UserId
}