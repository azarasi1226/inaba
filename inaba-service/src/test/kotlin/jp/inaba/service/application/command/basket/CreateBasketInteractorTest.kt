package jp.inaba.service.application.command.basket

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import jp.inaba.core.domain.basket.BasketId
import jp.inaba.core.domain.basket.CreateBasketError
import jp.inaba.core.domain.common.CommonError
import jp.inaba.core.domain.common.UseCaseException
import jp.inaba.core.domain.user.UserId
import jp.inaba.message.basket.command.CreateBasketCommand
import jp.inaba.service.domain.UniqueAggregateIdVerifier
import jp.inaba.service.domain.basket.CreateBasketVerifier
import jp.inaba.service.domain.basket.InternalCreateBasketCommand
import org.axonframework.commandhandling.gateway.CommandGateway
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class CreateBasketInteractorTest {
    @MockK
    private lateinit var uniqueAggregateIdVerifier: UniqueAggregateIdVerifier

    @MockK
    private lateinit var createBasketVerifier: CreateBasketVerifier

    @MockK
    private lateinit var commandGateway: CommandGateway

    @InjectMockKs
    private lateinit var sut: CreateBasketInteractor

    @AfterEach
    fun after() {
        confirmVerified(commandGateway)
    }

    @Test
    fun `ユーザーが存在_買い物かごを作成_Command発行`() {
        // Arrange
        val basketId = BasketId()
        val userId = UserId()
        val command =
            CreateBasketCommand(
                id = basketId,
                userId = userId,
            )
        every {
            uniqueAggregateIdVerifier.hasDuplicateAggregateId(basketId.value)
        } returns false
        every {
            createBasketVerifier.isUserNotFound(userId)
        } returns false
        every {
            createBasketVerifier.isLinkedToUser(userId)
        } returns false
        every {
            commandGateway.sendAndWait<Any>(any())
        } returns Unit

        // Act
        sut.handle(command)

        // Assert
        val expectCommand =
            InternalCreateBasketCommand(
                id = basketId,
                userId = userId,
            )
        verify(exactly = 1) {
            commandGateway.sendAndWait<Any>(expectCommand)
        }
    }

    @Test
    fun `AggregateIdが既に存在する_同じIDで買い物かごを作成する_Commandが発行されずException`() {
        // Arrange
        val basketId = BasketId()
        val userId = UserId()
        val command =
            CreateBasketCommand(
                id = basketId,
                userId = userId,
            )
        every {
            uniqueAggregateIdVerifier.hasDuplicateAggregateId(basketId.value)
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
    fun `ユーザーが存在しない_買い物かごを作成_Commandが発行されずException`() {
        // Arrange
        val basketId = BasketId()
        val userId = UserId()
        val command =
            CreateBasketCommand(
                id = basketId,
                userId = userId,
            )
        every {
            uniqueAggregateIdVerifier.hasDuplicateAggregateId(basketId.value)
        } returns false
        every {
            createBasketVerifier.isUserNotFound(userId)
        } returns true

        // Act
        val exception =
            assertThrows<UseCaseException> {
                sut.handle(command)
            }

        // Assert
        assert(exception.error == CreateBasketError.USER_NOT_FOUND)
    }

    @Test
    fun `登録したいUserIdで買い物かごが作成されている_買い物かごを作成_Commandが発行されずException`() {
        // Arrange
        val basketId = BasketId()
        val userId = UserId()
        val command =
            CreateBasketCommand(
                id = basketId,
                userId = userId,
            )
        every {
            uniqueAggregateIdVerifier.hasDuplicateAggregateId(basketId.value)
        } returns false
        every {
            createBasketVerifier.isUserNotFound(userId)
        } returns false
        every {
            createBasketVerifier.isLinkedToUser(userId)
        } returns true

        // Act
        val exception =
            assertThrows<UseCaseException> {
                sut.handle(command)
            }

        // Assert
        assert(exception.error == CreateBasketError.BASKET_ALREADY_LINKED_TO_USER)
    }
}
