package jp.inaba.service2

import org.axonframework.test.server.AxonServerContainer
import org.testcontainers.mysql.MySQLContainer
import org.testcontainers.utility.MountableFile
import java.nio.file.Paths

object AxonServerContainerFactory
{
  fun create(): AxonServerContainer =
    AxonServerContainer()
      .withAxonServerHostname("localhost")
      .withDcbContext(true)
      .withReuse(true)
      .withDevMode(true)
}