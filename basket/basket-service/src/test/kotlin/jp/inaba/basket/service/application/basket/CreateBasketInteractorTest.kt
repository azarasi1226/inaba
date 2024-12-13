package jp.inaba.basket.service.application.basket

import jp.inaba.basket.share.domain.basket.BasketId
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import jp.inaba.basket.api.domain.basket.command.CreateBasketCommand
import jp.inaba.basket.share.domain.basket.CreateBasketError
import jp.inaba.basket.service.application.command.basket.CreateBasketInteractor
import jp.inaba.basket.service.domain.basket.CanCreateBasketVerifier
import jp.inaba.basket.service.domain.basket.InternalCreateBasketCommand
import jp.inaba.common.domain.shared.ActionCommandResult
import jp.inaba.identity.share.domain.user.UserId
import org.axonframework.commandhandling.gateway.CommandGateway
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CreateBasketInteractorTest {
    @MockK
    private lateinit var canCreateBasketVerifier: CanCreateBasketVerifier

    @MockK
    private lateinit var commandGateway: CommandGateway

    @InjectMockKs
    private lateinit var sut: CreateBasketInteractor

    @BeforeEach
    fun before() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `ユーザーが存在_買い物かごを作成_InternalCommandが配送`() {
        val basketId = BasketId()
        val userId = UserId()
        val command =
            CreateBasketCommand(
                id = basketId,
                userId = userId,
            )
        every {
            canCreateBasketVerifier.checkUserExits(userId)
        } returns Ok(Unit)
        every {
            canCreateBasketVerifier.checkUserHasNoBasket(userId)
        } returns Ok(Unit)
        every {
            commandGateway.sendAndWait<ActionCommandResult>(any())
        } returns ActionCommandResult.ok()

        val result = sut.handle(command)

        assert(result.isOk())
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
    fun `ユーザーが存在しない_買い物かごを作成_InternalCommandが配送されずエラーが返る`() {
        val basketId = BasketId()
        val userId = UserId()
        val command =
            CreateBasketCommand(
                id = basketId,
                userId = userId,
            )
        every {
            canCreateBasketVerifier.checkUserExits(userId)
        } returns Err(CreateBasketError.USER_NOT_FOUND)

        val result = sut.handle(command)

        assert(!result.isOk())
        assert(result.errorCode == CreateBasketError.USER_NOT_FOUND.errorCode)
        verify(exactly = 0) {
            commandGateway.sendAndWait<Any>(any())
        }
    }

    @Test
    fun `ユーザーが存在しており、すでに買い物かごが登録されている_買い物かごを作成_InternalCommandが配送されずエラーが返る`() {
        val basketId = BasketId()
        val userId = UserId()
        val command =
            CreateBasketCommand(
                id = basketId,
                userId = userId,
            )
        every {
            canCreateBasketVerifier.checkUserExits(userId)
        } returns Ok(Unit)
        every {
            canCreateBasketVerifier.checkUserHasNoBasket(userId)
        } returns Err(CreateBasketError.BASKET_ALREADY_EXISTS)

        val result = sut.handle(command)

        assert(!result.isOk())
        assert(result.errorCode == CreateBasketError.BASKET_ALREADY_EXISTS.errorCode)
        verify(exactly = 0) {
            commandGateway.sendAndWait<Any>(any())
        }
    }
}
