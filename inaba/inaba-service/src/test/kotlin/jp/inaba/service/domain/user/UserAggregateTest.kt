package jp.inaba.service.domain.user

import jp.inaba.core.domain.basket.BasketId
import jp.inaba.core.domain.user.UserId
import jp.inaba.message.user.command.CreateUserCommand
import jp.inaba.message.user.command.LinkBasketIdCommand
import jp.inaba.message.user.command.LinkSubjectCommand
import jp.inaba.message.user.event.BasketIdLinkedEvent
import jp.inaba.message.user.event.SubjectLinkedEvent
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

        // Act
        fixture.givenNoPriorActivity()
            .`when`(
                CreateUserCommand(
                    id = userId,
                ),
            )
            // Assert
            .expectSuccessfulHandlerExecution()
            .expectEvents(
                UserCreatedEvent(
                    id = userId.value,
                ),
            )
    }

    @Test
    fun `ユーザーが作成済み_SubjectをLink_Event発行`() {
        // Arrange
        val userId = UserId()
        val subject = "cognit:123e4567-e89b-12d3-a456-426614174000"

        // Act
        fixture.given(
            UserCreatedEvent(id = userId.value),
        )
            .`when`(
                LinkSubjectCommand(
                    id = userId,
                    subject = subject,
                ),
            )
            // Assert
            .expectSuccessfulHandlerExecution()
            .expectEvents(
                SubjectLinkedEvent(
                    id = userId.value,
                    subject = subject,
                ),
            )
    }

    @Test
    fun `ユーザーが作成済み_BasketIdをLink_Event発行`() {
        // Arrange
        val userId = UserId()
        val basketId = BasketId()

        // Act
        fixture.given(
            UserCreatedEvent(id = userId.value),
        )
            .`when`(
                LinkBasketIdCommand(
                    id = userId,
                    basketId = basketId,
                ),
            )
            // Assert
            .expectSuccessfulHandlerExecution()
            .expectEvents(
                BasketIdLinkedEvent(
                    id = userId.value,
                    basketId = basketId.value,
                ),
            )
    }
}
