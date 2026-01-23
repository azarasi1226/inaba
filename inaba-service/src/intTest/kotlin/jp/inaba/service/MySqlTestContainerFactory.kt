package jp.inaba.service

import org.testcontainers.mysql.MySQLContainer
import org.testcontainers.utility.MountableFile
import java.nio.file.Paths

object MySqlTestContainerFactory {
    fun create(): MySQLContainer =
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
}
