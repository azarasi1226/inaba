package jp.inaba.service.application.command.user

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import jp.inaba.core.domain.common.CommonError
import jp.inaba.core.domain.common.UseCaseException
import jp.inaba.core.domain.user.CreateUserError
import jp.inaba.core.domain.user.UserId
import jp.inaba.message.user.command.CreateUserCommand
import jp.inaba.service.domain.UniqueAggregateIdVerifier
import jp.inaba.service.domain.user.CreateUserVerifier
import org.axonframework.commandhandling.gateway.CommandGateway
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class CreateUserInteractorTest {
    @MockK
    private lateinit var uniqueAggregateIdVerifier: UniqueAggregateIdVerifier

    @MockK
    private lateinit var createUserVerifier: CreateUserVerifier

    @MockK
    private lateinit var commandGateway: CommandGateway

    @InjectMockKs
    private lateinit var sut: CreateUserInteractor

    @AfterEach
    fun after() {
        confirmVerified(commandGateway)
    }

    @Test
    fun `ユーザーを作成_Commandが発行される`() {
        // Arrange
        val userId = UserId()
        val subject = "auth0|123456789"
        val command = CreateUserCommand(id = userId, subject = subject)
        every {
            uniqueAggregateIdVerifier.hasDuplicateAggregateId(userId.value)
        } returns false
        every {
            createUserVerifier.isLinkedSubject(subject)
        } returns false
        every {
            commandGateway.sendAndWait<Any>(any())
        } returns Unit

        // Act
        sut.handle(command)

        // Assert
        verify(exactly = 1) {
            commandGateway.sendAndWait<Any>(any())
        }
    }

    @Test
    fun `登録しようとしているIDで集約が作成済み_ユーザーを作成_Commandが発行されずException`() {
        // Arrange
        val userId = UserId()
        val subject = "auth0|123456789"
        val command = CreateUserCommand(id = userId, subject = subject)
        every {
            uniqueAggregateIdVerifier.hasDuplicateAggregateId(userId.value)
        } returns true

        // Act
        val exception =
            assertThrows<UseCaseException> {
                sut.handle(command)
            }

        // Assert
        assert(exception.error == CommonError.AGGREGATE_DUPLICATED)
    }

    @Test
    fun `Subjectが既に別のユーザーに紐づいている_ユーザーを作成_Commandが発行されずException`() {
        // Arrange
        val userId = UserId()
        val subject = "auth0|123456789"
        val command = CreateUserCommand(id = userId, subject = subject)
        every {
            uniqueAggregateIdVerifier.hasDuplicateAggregateId(userId.value)
        } returns false
        every {
            createUserVerifier.isLinkedSubject(subject)
        } returns true

        // Act
        val exception =
            assertThrows<UseCaseException> {
                sut.handle(command)
            }

        // Assert
        assert(exception.error == CreateUserError.USER_ALREADY_LINKED_TO_SUBJECT)
    }
}
