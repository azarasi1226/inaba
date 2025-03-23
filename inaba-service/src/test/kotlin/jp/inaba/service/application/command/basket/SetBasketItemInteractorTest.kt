package jp.inaba.service.application.command.basket

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import jp.inaba.core.domain.basket.BasketId
import jp.inaba.core.domain.basket.BasketItemQuantity
import jp.inaba.core.domain.basket.SetBasketItemError
import jp.inaba.core.domain.common.UseCaseException
import jp.inaba.core.domain.product.ProductId
import jp.inaba.message.basket.command.SetBasketItemCommand
import jp.inaba.service.domain.basket.InternalSetBasketItemCommand
import jp.inaba.service.domain.basket.SetBasketItemVerifier
import org.axonframework.commandhandling.gateway.CommandGateway
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class SetBasketItemInteractorTest {
    @MockK
    private lateinit var canSetBasketItemVerifier: SetBasketItemVerifier

    @MockK
    private lateinit var commandGateway: CommandGateway

    @InjectMockKs
    private lateinit var sut: SetBasketItemInteractor

    @AfterEach
    fun after() {
        confirmVerified(commandGateway)
    }

    @Test
    fun `商品が存在_買い物かごに商品を入れる_Command発行`() {
        // Arrange
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
            canSetBasketItemVerifier.isProductNotFound(productId)
        } returns false
        every {
            canSetBasketItemVerifier.isOutOfStock(productId, basketItemQuantity)
        } returns false
        every {
            commandGateway.sendAndWait<Any>(any())
        } returns Unit

        // Act
        sut.handle(command)

        // Assert
        val expectCommand =
            InternalSetBasketItemCommand(
                id = basketId,
                productId = productId,
                basketItemQuantity = basketItemQuantity,
            )
        verify(exactly = 1) {
            commandGateway.sendAndWait<Any>(expectCommand)
        }
    }

    @Test
    fun `商品が存在しない_買い物かごに商品を入れる_Commandが配送されずException`() {
        // Arrange
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
            canSetBasketItemVerifier.isProductNotFound(productId)
        } returns true

        // Act
        val exception =
            assertThrows<UseCaseException> {
                sut.handle(command)
            }

        // Assert
        assert(exception.error == SetBasketItemError.PRODUCT_NOT_FOUND)
    }

    @Test
    fun `商品はあるが、在庫が少ない_在庫数以上に買い物かごに商品を入れる_Commandが配送されずException`() {
        // Arrange
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
            canSetBasketItemVerifier.isProductNotFound(productId)
        } returns false
        every {
            canSetBasketItemVerifier.isOutOfStock(productId, basketItemQuantity)
        } returns true

        // Act
        val exception =
            assertThrows<UseCaseException> {
                sut.handle(command)
            }

        // Assert
        assert(exception.error == SetBasketItemError.OUT_OF_STOCK)
    }
}
