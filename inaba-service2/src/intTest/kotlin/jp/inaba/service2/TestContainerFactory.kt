package jp.inaba.service2

import org.axonframework.test.server.AxonServerContainer
import org.testcontainers.mysql.MySQLContainer
import org.testcontainers.utility.MountableFile
import java.nio.file.Paths

object TestContainerFactory {
    fun mysql(): MySQLContainer =
        MySQLContainer("mysql:8.0").apply {
            // DBの初期化スクリプトのパスを指定
            val hostPath =
                Paths
                    .get("../")
                    .toAbsolutePath()
                    .resolve("database/schema.mysql.sql")
            val mountable =
                MountableFile
                    .forHostPath(hostPath)
            withCopyFileToContainer(mountable, "/docker-entrypoint-initdb.d/schema.mysql.sql")
        }

    fun axonServer(): AxonServerContainer =
        AxonServerContainer()
            .withAxonServerHostname("localhost")
            .withDcbContext(true)
            .withReuse(true)
            .withDevMode(true)
}
