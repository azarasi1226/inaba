package jp.inaba.basket.service.application.basket

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import jp.inaba.basket.api.domain.basket.BasketCommands
import jp.inaba.basket.api.domain.basket.BasketErrors
import jp.inaba.basket.api.domain.basket.BasketId
import jp.inaba.basket.service.application.command.basket.CreateBasketInteractor
import jp.inaba.basket.service.domain.basket.CanCreateBasketVerifier
import jp.inaba.basket.service.domain.basket.InternalBasketCommands
import jp.inaba.identity.api.domain.user.UserId
import org.axonframework.commandhandling.gateway.CommandGateway
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class CreateBasketInteractorTest {
    @Mock
    private lateinit var canCreateBasketVerifier: CanCreateBasketVerifier

    @Mock
    private lateinit var commandGateway: CommandGateway

    @InjectMocks
    private lateinit var sut: CreateBasketInteractor

    @BeforeEach
    fun before() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `ユーザーが存在_買い物かごを作成_InternalCommandが配送`() {
        val userId = UserId()
        val command = BasketCommands.Create(userId)
        Mockito.`when`(canCreateBasketVerifier.checkUserExits(userId))
            .thenReturn(Ok(Unit))

        val result = sut.handle(command)

        assert(result.isOk())
        val expectCommand = InternalBasketCommands.Create(BasketId(userId))
        Mockito.verify(commandGateway, Mockito.only()).sendAndWait<Any>(expectCommand)
    }

    @Test
    fun `ユーザーが存在しない_買い物かごを作成_InternalCommandが配送されずエラーが返る`() {
        val userId = UserId()
        val command = BasketCommands.Create(userId)
        Mockito.`when`(canCreateBasketVerifier.checkUserExits(userId))
            .thenReturn(Err(BasketErrors.Create.USER_NOT_FOUND))

        val result = sut.handle(command)

        assert(!result.isOk())
        assert(result.errorCode == BasketErrors.Create.USER_NOT_FOUND.errorCode)
        val expectCommand = InternalBasketCommands.Create(BasketId(userId))
        Mockito.verify(commandGateway, Mockito.never()).sendAndWait<Any>(expectCommand)
    }
}
