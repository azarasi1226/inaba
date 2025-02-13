package jp.inaba.service.application.command.stock

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import jp.inaba.core.domain.common.UseCaseException
import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.stock.CreateStockError
import jp.inaba.core.domain.stock.StockId
import jp.inaba.message.stock.command.CreateStockCommand
import jp.inaba.service.domain.stock.CreateStockVerifier
import jp.inaba.service.domain.stock.InternalCreateStockCommand
import org.axonframework.commandhandling.gateway.CommandGateway
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CreateStockInteractorTest {
    @MockK
    private lateinit var canCreateStockVerifier: CreateStockVerifier

    @MockK
    private lateinit var commandGateway: CommandGateway

    @InjectMockKs
    private lateinit var sut: CreateStockInteractor

    @BeforeEach
    fun before() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `商品が存在_在庫を作成_Command発行`() {
        // Arrange
        val stockId = StockId()
        val productId = ProductId()
        val command =
            CreateStockCommand(
                id = stockId,
                productId = productId,
            )
        every {
            canCreateStockVerifier.isProductNotFound(productId)
        } returns false
        every {
            commandGateway.sendAndWait<Any>(any())
        } returns Unit

        // Act
        sut.handle(command)

        // Assert
        val expectCommand =
            InternalCreateStockCommand(
                id = stockId,
                productId = productId,
            )
        verify(exactly = 1) {
            commandGateway.sendAndWait<Any>(expectCommand)
        }
    }

    @Test
    fun `商品が存在しない_在庫を作成_Commandが発行されずException`() {
        // Arrange
        val stockId = StockId()
        val productId = ProductId()
        val command =
            CreateStockCommand(
                id = stockId,
                productId = productId,
            )
        every {
            canCreateStockVerifier.isProductNotFound(productId)
        } returns true
        every {
            commandGateway.sendAndWait<Any>(any())
        } returns Unit

        // Act
        val exception =
            assertThrows<UseCaseException> {
                sut.handle(command)
            }

        // Assert
        assert(exception.error == CreateStockError.ProductNotExits)
        verify(exactly = 0) {
            commandGateway.sendAndWait<Any>(any())
        }
    }
}
