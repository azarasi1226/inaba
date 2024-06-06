package jp.inaba.basket.service.application.basket

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import jp.inaba.basket.api.domain.basket.BasketId
import jp.inaba.basket.api.domain.basket.BasketItemQuantity
import jp.inaba.basket.api.domain.basket.SetBasketItemCommand
import jp.inaba.basket.api.domain.basket.SetBasketItemError
import jp.inaba.basket.service.application.command.basket.SetBasketItemInteractor
import jp.inaba.basket.service.domain.basket.CanSetBasketItemVerifier
import jp.inaba.basket.service.domain.basket.InternalCreateBasketCommand
import jp.inaba.basket.service.domain.basket.InternalSetBasketItemCommand
import jp.inaba.catalog.api.domain.product.ProductId
import jp.inaba.common.domain.shared.ActionCommandResult
import jp.inaba.identity.api.domain.user.UserId
import org.axonframework.commandhandling.gateway.CommandGateway
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
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
    fun `商品が存在_商品を買い物かごに入れる_InternalCommandが配送`() {
        val basketId = BasketId(UserId())
        val productId = ProductId()
        val basketItemQuantity = BasketItemQuantity(1)
        val command =
            SetBasketItemCommand(
                id = basketId,
                productId = productId,
                basketItemQuantity = basketItemQuantity,
            )
        Mockito.`when`(canSetBasketItemVerifier.checkProductExits(productId))
            .thenReturn(Ok(Unit))
        Mockito.`when`(commandGateway.sendAndWait<ActionCommandResult>(any()))
            .thenReturn(ActionCommandResult.ok())

        val result = sut.handle(command)

        assert(result.isOk())
        val expectCommand =
            InternalSetBasketItemCommand(
                id = basketId,
                productId = productId,
                basketItemQuantity = basketItemQuantity,
            )
        Mockito.verify(commandGateway, Mockito.only()).sendAndWait<ActionCommandResult>(expectCommand)
    }

    @Test
    fun `商品が存在しない_商品を買い物かごに入れる_InternalCommandが配送されずエラーが返る`() {
        val basketId = BasketId(UserId())
        val productId = ProductId()
        val basketItemQuantity = BasketItemQuantity(1)
        val command =
            SetBasketItemCommand(
                id = basketId,
                productId = productId,
                basketItemQuantity = basketItemQuantity,
            )
        Mockito.`when`(canSetBasketItemVerifier.checkProductExits(productId))
            .thenReturn(Err(SetBasketItemError.PRODUCT_NOT_FOUND))

        val result = sut.handle(command)

        assert(!result.isOk())
        assert(result.errorCode == SetBasketItemError.PRODUCT_NOT_FOUND.errorCode)
        val expectCommand =
            InternalSetBasketItemCommand(
                id = basketId,
                productId = productId,
                basketItemQuantity = basketItemQuantity,
            )
        Mockito.verify(commandGateway, Mockito.never()).sendAndWait<ActionCommandResult>(expectCommand)
    }
}
