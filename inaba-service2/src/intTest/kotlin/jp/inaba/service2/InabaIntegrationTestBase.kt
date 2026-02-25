package jp.inaba.service2

import org.axonframework.common.configuration.ApplicationConfigurer
import org.axonframework.test.fixture.AxonTestFixture
import org.axonframework.test.fixture.MessagesRecordingConfigurationEnhancer
import org.axonframework.test.server.AxonServerContainer
import org.axonframework.test.server.AxonServerContainerUtils
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(classes = [InabaApplication::class])
@ActiveProfiles("integration-test")
@Testcontainers
@Import(AxonTestConfig::class)
abstract class InabaIntegrationTestBase {
    @Autowired
    lateinit var configurer: ApplicationConfigurer

    lateinit var fixture: AxonTestFixture

    companion object {
        @Container
        @ServiceConnection
        val mysql = MySqlTestContainerFactory.create()

        @Container
        @ServiceConnection
        val axonServer = AxonServerContainerFactory.create()

        @JvmStatic
        @DynamicPropertySource
        fun axonServerProperties(registry: DynamicPropertyRegistry) {
            //registry.add("axon.axonserver.servers") { axonServer.axonServerAddress }
            //registry.add("axon.axonserver.enabled") { true }
        }

        @JvmStatic
        @BeforeAll
        fun setup2() {
            // EventStoreの初期化
//            AxonServerContainerUtils.purgeEventsFromAxonServer(
//                axonServer.host,
//                axonServer.httpPort,
//                "default",
//                true,
//            )
        }
    }

    @BeforeEach
    fun setup() {
        // Fixtureの初期化
        fixture = AxonTestFixture.with(configurer)
    }

    @AfterEach
    fun tearDown() {
        fixture.stop()
    }
}

@TestConfiguration
class AxonTestConfig {
    @Bean
    fun messagesRecordingConfigurationEnhancer() = MessagesRecordingConfigurationEnhancer()
}
