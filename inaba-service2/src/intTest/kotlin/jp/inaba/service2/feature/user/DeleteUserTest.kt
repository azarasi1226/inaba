package jp.inaba.service2.feature.user

import jp.inaba.core.domain.user.UserId
import jp.inaba.message.user.command.DeleteUserCommand
import jp.inaba.message.user.command.DeleteUserResult
import jp.inaba.message.user.event.UserCreatedEvent
import jp.inaba.message.user.event.UserDeletedEvent
import jp.inaba.service2.feature.InabaIntegrationTestBase
import org.junit.jupiter.api.Test

class DeleteUserTest : InabaIntegrationTestBase() {
    @Test
    fun `正常系_ユーザー削除成功`() {
        val userId = UserId()

        fixture
            .given()
            .events(
                UserCreatedEvent(
                    id = userId.value,
                    subject = "test-subject",
                ),
            ).`when`()
            .command(
                DeleteUserCommand(
                    id = userId,
                ),
            ).then()
            .resultMessagePayload(DeleteUserResult.success())
            .events(
                UserDeletedEvent(
                    id = userId.value,
                ),
            )
    }

    @Test
    fun `ユーザーが存在しない場合_notFound`() {
        val userId = UserId()

        fixture
            .given()
            .noPriorActivity()
            .`when`()
            .command(
                DeleteUserCommand(
                    id = userId,
                ),
            ).then()
            .resultMessagePayload(DeleteUserResult.notFound())
            .noEvents()
    }

    @Test
    fun `すでに削除済みの場合_冪等性により成功を返す`() {
        val userId = UserId()

        fixture
            .given()
            .events(
                UserCreatedEvent(
                    id = userId.value,
                    subject = "test-subject",
                ),
                UserDeletedEvent(
                    id = userId.value,
                ),
            ).`when`()
            .command(
                DeleteUserCommand(
                    id = userId,
                ),
            ).then()
            .resultMessagePayload(DeleteUserResult.success())
            .noEvents()
    }
}
