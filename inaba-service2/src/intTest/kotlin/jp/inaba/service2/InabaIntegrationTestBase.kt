package jp.inaba.service2

import org.axonframework.common.configuration.ApplicationConfigurer
import org.axonframework.test.fixture.AxonTestFixture
import org.axonframework.test.fixture.MessagesRecordingConfigurationEnhancer
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

@SpringBootTest(classes = [InabaApplication::class])
@ActiveProfiles("integration-test")
@Import(AxonTestConfig::class)
abstract class InabaIntegrationTestBase {
    @Autowired
    lateinit var configurer: ApplicationConfigurer

    lateinit var fixture: AxonTestFixture

    companion object {
        @ServiceConnection
        val mysql = TestContainerFactory.mysql().apply { start() }

        @ServiceConnection
        val axonServer = TestContainerFactory.axonServer().apply { start() }

        @JvmStatic
        @BeforeAll
        fun beforeTestSuite() {
            // EventStoreの初期化
            AxonServerContainerUtils.purgeEventsFromAxonServer(
                axonServer.host,
                axonServer.httpPort,
                "default",
                true,
            )

            // TODO:MySQLの初期化
        }
    }

    @BeforeEach
    fun beforeTestCase() {
        fixture = AxonTestFixture.with(configurer)
    }

    @AfterEach
    fun afterTestCase() {
        fixture.stop()
    }
}

@TestConfiguration
class AxonTestConfig {
    @Bean
    fun messagesRecordingConfigurationEnhancer() = MessagesRecordingConfigurationEnhancer()
}
