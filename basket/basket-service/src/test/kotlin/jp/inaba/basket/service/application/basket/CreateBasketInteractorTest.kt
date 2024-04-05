package jp.inaba.basket.service.application.basket

import jp.inaba.basket.api.domain.basket.BasketCommands
import jp.inaba.basket.api.domain.basket.BasketId
import jp.inaba.basket.service.application.command.basket.create.CreateBasketInteractor
import jp.inaba.basket.service.application.command.basket.create.UserNotFoundException
import jp.inaba.basket.service.domain.basket.CanCreateBasketVerifier
import jp.inaba.basket.service.domain.basket.InternalBasketCommands
import jp.inaba.identity.api.domain.user.UserId
import org.axonframework.commandhandling.gateway.CommandGateway
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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
    fun ユーザーが存在する_バスケットを作成する_InternalCommand配送() {
        val userId = UserId()
        val command = BasketCommands.Create(userId)
        Mockito.`when`(canCreateBasketVerifier.existUser(userId))
            .thenReturn(true)

        sut.handle(command)

        val expectCommand = InternalBasketCommands.Create(BasketId(userId))
        Mockito.verify(commandGateway).sendAndWait<Any>(expectCommand)
    }

    @Test
    fun ユーザーが存在しない_バスケットを作成する_例外() {
        val userId = UserId()
        val command = BasketCommands.Create(userId)
        Mockito.`when`(canCreateBasketVerifier.existUser(userId))
            .thenReturn(false)

        assertThrows<UserNotFoundException> {
            sut.handle(command)
        }

        val expectCommand = InternalBasketCommands.Create(BasketId(userId))
        Mockito.verify(commandGateway, Mockito.never()).sendAndWait<Any>(expectCommand)
    }
}