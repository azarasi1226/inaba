package jp.inaba.service.domain.user

import jp.inaba.core.domain.user.UserId
import jp.inaba.message.user.event.UserCreatedEvent
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UserAggregateTest {
    private lateinit var fixture: FixtureConfiguration<UserAggregate>

    @BeforeEach
    fun before() {
        fixture = AggregateTestFixture(UserAggregate::class.java)
    }

    @Test
    fun `ユーザー作成_Event発行`() {
        // Arrange
        val userId = UserId()
        val subject = "oidc:sub:azarasikozou"

        // Act
        fixture.givenNoPriorActivity()
            .`when`(
                InternalCreateUserCommand(
                    id = userId,
                    subject = subject,
                ),
            )
            // Assert
            .expectSuccessfulHandlerExecution()
            .expectEvents(
                UserCreatedEvent(
                    id = userId.value,
                    subject = subject,
                ),
            )
    }
}
