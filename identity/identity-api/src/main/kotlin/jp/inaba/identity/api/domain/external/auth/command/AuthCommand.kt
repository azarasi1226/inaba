package jp.inaba.identity.api.domain.external.auth.command

import org.axonframework.commandhandling.RoutingKey

interface AuthCommand {
    @get:RoutingKey
    val emailAddress: String
}