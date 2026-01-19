package jp.inaba.service

import de.huxhorn.sulky.ulid.ULID
import jp.inaba.core.domain.user.UserId
import jp.inaba.message.user.command.CreateUserCommand
import org.axonframework.commandhandling.gateway.CommandGateway
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
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
                //     withInitScript("")
            }
    }
}

class Test1 : IntegrationTestBase() {
    @Autowired
    lateinit var commandGateway: CommandGateway

    @Test
    fun `test1`() {
        val command =
            CreateUserCommand(
                id = UserId(),
                subject = ULID().nextULID(),
            )

        commandGateway.sendAndWait<Any>(command)
    }
}

class Test2 : IntegrationTestBase() {
    @Test
    fun `test2`() {
        println("name" + mysql.containerName)
    }
}

class Test3 : IntegrationTestBase() {
    @Test
    fun `test3`() {
        println("name" + mysql.containerName)
    }
}

class Test4 : IntegrationTestBase() {
    @Test
    fun `test4`() {
        println("name" + mysql.containerName)
    }
}
