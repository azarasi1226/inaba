package jp.inaba.service.user

import jp.inaba.core.domain.user.CreateUserError
import jp.inaba.core.domain.user.UserId
import jp.inaba.message.user.command.CreateUserCommand
import jp.inaba.message.user.query.FindUserMetadataBySubjectQuery
import jp.inaba.message.user.query.FindUserMetadataBySubjectResult
import jp.inaba.service.MySqlTestContainerFactory
import jp.inaba.service.fixture.UserTestDataCreator
import jp.inaba.service.utlis.getWrapUseCaseError
import jp.inaba.service.utlis.isWrapUseCaseError
import jp.inaba.service.utlis.retryQuery
import org.axonframework.commandhandling.CommandExecutionException
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.queryhandling.QueryGateway
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@ActiveProfiles("integration-test")
@Testcontainers
class CreateUserIntegrationTest(
    @param:Autowired
    private val commandGateway: CommandGateway,
    @param:Autowired
    private val queryGateway: QueryGateway,
) {
    companion object {
        @Container
        @ServiceConnection
        val mysql = MySqlTestContainerFactory.create()
    }

    val userTestDataCreator = UserTestDataCreator(commandGateway)

    @Test
    fun `ユーザーを作成する_成功する`() {
        // Arrange
        val createUserCommand =
            CreateUserCommand(
                id = UserId(),
                subject = "aaa",
            )

        // Act
        commandGateway.sendAndWait<Any>(createUserCommand)

        // Assert
        val request = FindUserMetadataBySubjectQuery(createUserCommand.subject)
        // 結果整合性により、すぐ問い合わせてもまだプロジェクションされて無い可能性があるのでリトライする
        // TODO: ここはFindUserByIdQueryで確認するようにしたい
        val result = queryGateway.retryQuery<FindUserMetadataBySubjectResult, FindUserMetadataBySubjectQuery>(request)
        assert(result.userId == createUserCommand.id.value)
    }

    @Test
    fun `すでにユーザーが登録されている_同じSubjectでユーザーを登録する_UseCaseError`() {
        // Arrange
        userTestDataCreator.handle(subject = "aaa2")
        val createUserCommand =
            CreateUserCommand(
                id = UserId(),
                subject = "aaa2",
            )

        // Act
        val exception =
            assertThrows<CommandExecutionException> {
                commandGateway.sendAndWait<Any>(createUserCommand)
            }

        // Assert
        assert(exception.isWrapUseCaseError())
        assert(exception.getWrapUseCaseError().errorCode == CreateUserError.USER_ALREADY_LINKED_TO_SUBJECT.errorCode)
    }
}
