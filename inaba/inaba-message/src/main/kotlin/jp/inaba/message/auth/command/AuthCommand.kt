package jp.inaba.message.auth.command

import org.axonframework.commandhandling.RoutingKey

interface AuthCommand {
    @get:RoutingKey
    val emailAddress: String
}
