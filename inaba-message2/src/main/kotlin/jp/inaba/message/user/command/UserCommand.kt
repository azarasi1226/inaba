package jp.inaba.message.user.command

import jp.inaba.core.domain.user.UserId
import org.axonframework.modelling.annotation.TargetEntityId

interface UserCommand {
    @get:TargetEntityId
    val id: UserId
}
