package jp.inaba.basket.service.application.basket

import jp.inaba.basket.api.domain.basket.BasketCommands
import jp.inaba.basket.api.domain.basket.BasketId
import jp.inaba.basket.api.domain.basket.BasketItemQuantity
import jp.inaba.basket.service.application.command.basket.setbasketitem.ProductNotFoundException
import jp.inaba.basket.service.application.command.basket.setbasketitem.SetBasketItemInteractor
import jp.inaba.basket.service.domain.basket.CanSetBasketItemVerifier
import jp.inaba.basket.service.domain.basket.InternalBasketCommands
import jp.inaba.catalog.api.domain.product.ProductId
import jp.inaba.identity.api.domain.user.UserId
import org.axonframework.commandhandling.gateway.CommandGateway
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class SetBasketItemInteractorTest {
    @Mock
    private lateinit var canSetBasketItemVerifier: CanSetBasketItemVerifier
    @Mock
    private lateinit var commandGateway: CommandGateway
    @InjectMocks
    private lateinit var sut: SetBasketItemInteractor

    @BeforeEach
    fun before() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun 商品が存在する_商品をバスケットにセットする_InternalCommand配送() {
        val basketId = BasketId(UserId())
        val productId = ProductId()
        val basketItemQuantity = BasketItemQuantity(1)
        val command = BasketCommands.SetBasketItem(
            id = basketId,
            productId = productId,
            basketItemQuantity = basketItemQuantity
        )
        Mockito.`when`(canSetBasketItemVerifier.existProduct(productId))
            .thenReturn(true)

        sut.handle(command)

        val expectCommand = InternalBasketCommands.SetBasketItem(
            id = basketId,
            productId = productId,
            basketItemQuantity = basketItemQuantity
        )
        Mockito.verify(commandGateway).sendAndWait<Any>(expectCommand)
    }

    @Test
    fun ユーザーが存在しない_バスケットを作成する_例外() {
        val basketId = BasketId(UserId())
        val productId = ProductId()
        val basketItemQuantity = BasketItemQuantity(1)
        val command = BasketCommands.SetBasketItem(
            id = basketId,
            productId = productId,
            basketItemQuantity = basketItemQuantity
        )
        Mockito.`when`(canSetBasketItemVerifier.existProduct(productId))
            .thenReturn(false)

        assertThrows<ProductNotFoundException> {
            sut.handle(command)
        }

        val expectCommand = InternalBasketCommands.SetBasketItem(
            id = basketId,
            productId = productId,
            basketItemQuantity = basketItemQuantity
        )
        Mockito.verify(commandGateway, Mockito.never()).sendAndWait<Any>(expectCommand)
    }
}