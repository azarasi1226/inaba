package jp.inaba.basket.service.application.basket

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import jp.inaba.basket.api.domain.basket.BasketId
import jp.inaba.basket.api.domain.basket.CreateBasketCommand
import jp.inaba.basket.api.domain.basket.CreateBasketError
import jp.inaba.basket.service.application.command.basket.CreateBasketInteractor
import jp.inaba.basket.service.domain.basket.CanCreateBasketVerifier
import jp.inaba.basket.service.domain.basket.InternalCreateBasketCommand
import jp.inaba.common.domain.shared.ActionCommandResult
import jp.inaba.identity.api.domain.user.UserId
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
        val userId = UserId()
        val command = CreateBasketCommand(userId)
        every {
            canCreateBasketVerifier.checkUserExits(userId)
        } returns Ok(Unit)
        every {
            commandGateway.sendAndWait<ActionCommandResult>(any())
        } returns ActionCommandResult.ok()

        val result = sut.handle(command)

        assert(result.isOk())
        val expectCommand = InternalCreateBasketCommand(BasketId(userId))
        verify(exactly = 1) {
           commandGateway.sendAndWait<Any>(expectCommand)
        }
    }

    @Test
    fun `ユーザーが存在しない_買い物かごを作成_InternalCommandが配送されずエラーが返る`() {
        val userId = UserId()
        val command = CreateBasketCommand(userId)
        every {
            canCreateBasketVerifier.checkUserExits(userId)
        } returns Err(CreateBasketError.USER_NOT_FOUND)

        val result = sut.handle(command)

        assert(!result.isOk())
        assert(result.errorCode == CreateBasketError.USER_NOT_FOUND.errorCode)
        val expectCommand = InternalCreateBasketCommand(BasketId(userId))
        verify(exactly = 0) {
            commandGateway.sendAndWait<Any>(expectCommand)
        }
    }
}
