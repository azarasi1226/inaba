package jp.inaba.service

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.test.context.TestPropertySource
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.mysql.MySQLContainer

// @TestContainers & @Containerにより自動的にコンテナのライフサイクルが管理される
// @ServiceConnectionによりSpring BootのDataSourceに自動的に接続情報が注入される
@SpringBootTest
@Testcontainers
@TestPropertySource(
    properties = [
        // 起動に最小限必要なパラメーター
        "spring.application.name=inaba-integration-test",
        "spring.profiles.active=local",
        // Test高速化の観点からAxonServerは無効化Inmemoryで済ます
        "axon.axonserver.enabled=false",
        // gRPCとHTTPのポートは0にしてランダム割り当てる。これによりテスト並列化が可能になる
        "grpc.server.port=0",
        "server.port=0",
    ],
)
class IntegrationTestBase {
    companion object {
        @Container
        @ServiceConnection
        val mysql =
            MySQLContainer("mysql:8.0").apply {
                // DBの初期化スクリプトのパスを指定
                val hostPath =
                    java.nio.file.Paths
                        .get("../")
                        .toAbsolutePath()
                        .resolve("database/schema.mysql.sql")
                val mountable =
                    org.testcontainers.utility.MountableFile
                        .forHostPath(hostPath)
                withCopyFileToContainer(mountable, "/docker-entrypoint-initdb.d/schema.mysql.sql")
            }
    }
}
