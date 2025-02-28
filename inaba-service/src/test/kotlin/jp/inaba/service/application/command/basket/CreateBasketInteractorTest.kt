package jp.inaba.service.application.command.basket

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import jp.inaba.core.domain.basket.BasketId
import jp.inaba.core.domain.basket.CreateBasketError
import jp.inaba.core.domain.common.UseCaseException
import jp.inaba.core.domain.user.UserId
import jp.inaba.message.basket.command.CreateBasketCommand
import jp.inaba.service.domain.basket.CreateBasketVerifier
import jp.inaba.service.domain.basket.InternalCreateBasketCommand
import org.axonframework.commandhandling.gateway.CommandGateway
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CreateBasketInteractorTest {
    @MockK
    private lateinit var canCreateBasketVerifier: CreateBasketVerifier

    @MockK
    private lateinit var commandGateway: CommandGateway

    @InjectMockKs
    private lateinit var sut: CreateBasketInteractor

    @BeforeEach
    fun before() {
        MockKAnnotations.init(this)
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
            canCreateBasketVerifier.isUserNotFound(userId)
        } returns false
        every {
            canCreateBasketVerifier.isLinkedToUser(userId)
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
            canCreateBasketVerifier.isUserNotFound(userId)
        } returns true

        // Act
        val exception =
            assertThrows<UseCaseException> {
                sut.handle(command)
            }

        // Assert
        assert(exception.error == CreateBasketError.USER_NOT_FOUND)
        verify(exactly = 0) {
            commandGateway.sendAndWait<Any>(any())
        }
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
            canCreateBasketVerifier.isUserNotFound(userId)
        } returns false
        every {
            canCreateBasketVerifier.isLinkedToUser(userId)
        } returns true

        // Act
        val exception =
            assertThrows<UseCaseException> {
                sut.handle(command)
            }

        // Assert
        assert(exception.error == CreateBasketError.BASKET_ALREADY_EXISTS)
        verify(exactly = 0) {
            commandGateway.sendAndWait<Any>(any())
        }
    }
}
