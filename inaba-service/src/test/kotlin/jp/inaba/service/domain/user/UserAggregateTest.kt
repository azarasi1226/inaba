package jp.inaba.service.domain.user

import jp.inaba.core.domain.user.UserId
import jp.inaba.message.user.command.DeleteUserCommand
import jp.inaba.message.user.event.UserCreatedEvent
import jp.inaba.message.user.event.UserDeletedEvent
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
    fun `ユーザーを作成_Event発行`() {
        // Arrange
        val userId = UserId()
        val subject = "auth0|123456789"

        fixture
            .givenNoPriorActivity()
            // Act
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

    @Test
    fun `ユーザーを削除_Event発行`() {
        // Arrange
        val userId = UserId()
        val subject = "auth0|123456789"

        fixture
            .given(
                UserCreatedEvent(
                    id = userId.value,
                    subject = subject,
                ),
            )
            // Act
            .`when`(
                DeleteUserCommand(
                    id = userId,
                ),
            )
            // Assert
            .expectSuccessfulHandlerExecution()
            .expectEvents(
                UserDeletedEvent(
                    id = userId.value,
                ),
            )
    }
}

