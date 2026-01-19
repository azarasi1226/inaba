package jp.inaba.service

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.TestPropertySource
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.mysql.MySQLContainer

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(
    properties = [
        "APPLICATION_NAME=inaba-service",
        "SPRING_PROFILES_ACTIVE=local",
    ],
)
@Testcontainers
class IntegrationTestBase {
    @LocalServerPort
    private var port: Int = 0

    companion object {
        @Container
        @ServiceConnection
        val mysql =
            MySQLContainer("mysql:8.0").apply {
                //     withInitScript("")
            }

        @JvmStatic
        @DynamicPropertySource
        fun registerProperties(registry: DynamicPropertyRegistry) {
            //   registry.add("SERVER_PORT") { "0" }
            registry.add("GRPC_PORT") { "-1" }
            registry.add("axon.axonserver.enabled") { "false" }
        }
    }

    fun cleanup() {
    }
}

class Test1 : IntegrationTestBase() {
    @Test
    fun `test1`() {
        println("name" + mysql.containerName)
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
