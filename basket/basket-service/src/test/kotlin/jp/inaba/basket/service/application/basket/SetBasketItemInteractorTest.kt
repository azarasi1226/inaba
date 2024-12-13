package jp.inaba.basket.service.application.basket

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import jp.inaba.basket.api.domain.basket.command.SetBasketItemCommand
import jp.inaba.basket.service.application.command.basket.SetBasketItemInteractor
import jp.inaba.basket.service.domain.basket.CanSetBasketItemVerifier
import jp.inaba.basket.service.domain.basket.InternalSetBasketItemCommand
import jp.inaba.basket.share.domain.basket.BasketId
import jp.inaba.basket.share.domain.basket.BasketItemQuantity
import jp.inaba.basket.share.domain.basket.SetBasketItemError
import jp.inaba.catalog.api.domain.product.ProductId
import jp.inaba.common.domain.shared.ActionCommandResult
import org.axonframework.commandhandling.gateway.CommandGateway
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SetBasketItemInteractorTest {
    @MockK
    private lateinit var canSetBasketItemVerifier: CanSetBasketItemVerifier

    @MockK
    private lateinit var commandGateway: CommandGateway

    @InjectMockKs
    private lateinit var sut: SetBasketItemInteractor

    @BeforeEach
    fun before() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `商品が存在_商品を買い物かごに入れる_InternalCommandが配送`() {
        val basketId = BasketId()
        val productId = ProductId()
        val basketItemQuantity = BasketItemQuantity(1)
        val command =
            SetBasketItemCommand(
                id = basketId,
                productId = productId,
                basketItemQuantity = basketItemQuantity,
            )
        every {
            canSetBasketItemVerifier.checkProductExits(productId)
        } returns Ok(Unit)
        every {
            commandGateway.sendAndWait<ActionCommandResult>(any())
        } returns ActionCommandResult.ok()

        val result = sut.handle(command)

        assert(result.isOk())
        val expectCommand =
            InternalSetBasketItemCommand(
                id = basketId,
                productId = productId,
                basketItemQuantity = basketItemQuantity,
            )
        verify(exactly = 1) {
            commandGateway.sendAndWait<ActionCommandResult>(expectCommand)
        }
    }

    @Test
    fun `商品が存在しない_商品を買い物かごに入れる_InternalCommandが配送されずエラーが返る`() {
        val basketId = BasketId()
        val productId = ProductId()
        val basketItemQuantity = BasketItemQuantity(1)
        val command =
            SetBasketItemCommand(
                id = basketId,
                productId = productId,
                basketItemQuantity = basketItemQuantity,
            )
        every {
            canSetBasketItemVerifier.checkProductExits(productId)
        } returns Err(SetBasketItemError.PRODUCT_NOT_FOUND)

        val result = sut.handle(command)

        assert(!result.isOk())
        assert(result.errorCode == SetBasketItemError.PRODUCT_NOT_FOUND.errorCode)
        val expectCommand =
            InternalSetBasketItemCommand(
                id = basketId,
                productId = productId,
                basketItemQuantity = basketItemQuantity,
            )
        verify(exactly = 0) {
            commandGateway.sendAndWait<ActionCommandResult>(expectCommand)
        }
    }
}
