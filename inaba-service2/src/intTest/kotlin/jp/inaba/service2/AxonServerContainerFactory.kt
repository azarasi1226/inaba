package jp.inaba.service2

import org.axonframework.test.server.AxonServerContainer

object AxonServerContainerFactory {
    fun create(): AxonServerContainer =
        AxonServerContainer()
            .withAxonServerHostname("localhost")
            .withDcbContext(true)
            .withReuse(true)
            .withDevMode(true)
}
