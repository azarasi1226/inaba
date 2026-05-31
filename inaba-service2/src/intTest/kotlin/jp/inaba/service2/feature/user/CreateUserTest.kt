package jp.inaba.service2.feature.user

import jp.inaba.core.domain.user.UserId
import jp.inaba.message.user.command.CreateUserCommand
import jp.inaba.message.user.command.CreateUserResult
import jp.inaba.message.user.event.UserCreatedEvent
import jp.inaba.service2.feature.InabaIntegrationTestBase
import org.junit.jupiter.api.Test

class CreateUserTest : InabaIntegrationTestBase() {
    @Test
    fun `正常系_ユーザー作成成功`() {
        val userId = UserId()

        fixture
            .given()
            .noPriorActivity()
            .`when`()
            .command(
                CreateUserCommand(
                    id = userId,
                    subject = "test-subject",
                ),
            ).then()
            .resultMessagePayload(CreateUserResult.success())
            .events(
                UserCreatedEvent(
                    id = userId.value,
                    subject = "test-subject",
                ),
            )
    }

    @Test
    fun `すでに同じIDで登録済み_alreadyExists`() {
        val userId = UserId()

        fixture
            .given()
            .events(
                UserCreatedEvent(
                    id = userId.value,
                    subject = "existing-subject",
                ),
            ).`when`()
            .command(
                CreateUserCommand(
                    id = userId,
                    subject = "new-subject",
                ),
            ).then()
            .resultMessagePayload(CreateUserResult.alreadyExists())
            .noEvents()
    }
}
