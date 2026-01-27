package jp.inaba.service.basket

import jp.inaba.core.domain.basket.BasketItemQuantity
import jp.inaba.core.domain.basket.SetBasketItemError
import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.product.StockQuantity
import jp.inaba.message.basket.command.SetBasketItemCommand
import jp.inaba.service.MySqlTestContainerFactory
import jp.inaba.service.fixture.BasketTestDataCreator
import jp.inaba.service.fixture.ProductTestDataCreator
import jp.inaba.service.utlis.getWrapUseCaseError
import jp.inaba.service.utlis.isWrapUseCaseError
import org.axonframework.commandhandling.CommandExecutionException
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.queryhandling.QueryGateway
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@ActiveProfiles("integration-test")
@Testcontainers
class SetBasketItemIntegrationTest(
    @param:Autowired
    private val commandGateway: CommandGateway,
    @param:Autowired
    private val queryGateway: QueryGateway,
) {
    companion object {
        @Container
        @ServiceConnection
        private val mysql = MySqlTestContainerFactory.create()
    }

    private val basketTestDataCreator = BasketTestDataCreator(commandGateway)
    private val productTestDataCreator = ProductTestDataCreator(commandGateway)

    @Test
    fun `買い物かごに商品を追加する_成功`() {
        // Arrange
        val basketId = basketTestDataCreator.create()
        val productId = productTestDataCreator.create()
        val setBasketItemCommand =
            SetBasketItemCommand(
                id = basketId,
                productId = productId,
                basketItemQuantity = BasketItemQuantity(1),
            )

        // Act & Assert
        assertDoesNotThrow {
            commandGateway.sendAndWait<Any>(setBasketItemCommand)
        }
    }

    @Test
    fun `存在しない商品を買い物かごに追加する_失敗`() {
        // Arrange
        val basketId = basketTestDataCreator.create()
        val missingProductId = ProductId()
        val setBasketItemCommand =
            SetBasketItemCommand(
                id = basketId,
                productId = missingProductId,
                basketItemQuantity = BasketItemQuantity(2),
            )

        // Act
        val exception =
            assertThrows<CommandExecutionException> {
                commandGateway.sendAndWait<Any>(setBasketItemCommand)
            }

        // Assert
        assert(exception.isWrapUseCaseError())
        assert(exception.getWrapUseCaseError().errorCode == SetBasketItemError.PRODUCT_NOT_FOUND.errorCode)
    }

    @Test
    fun `バスケットに追加した商品の個数が商品の在庫数を上回っていた_失敗`() {
        // Arrange
        val basketId = basketTestDataCreator.create()
        val productId = productTestDataCreator.create(quantity = StockQuantity(5))
        val setBasketItemCommand =
            SetBasketItemCommand(
                id = basketId,
                productId = productId,
                basketItemQuantity = BasketItemQuantity(10),
            )

        // Act
        val exception =
            assertThrows<CommandExecutionException> {
                commandGateway.sendAndWait<Any>(setBasketItemCommand)
            }

        // Assert
        assert(exception.isWrapUseCaseError())
        assert(exception.getWrapUseCaseError().errorCode == SetBasketItemError.OUT_OF_STOCK.errorCode)
    }

    @Test
    fun `バスケットに追加した商品種類の合計が50を越した_失敗`() {
        // Arrange
        val basketId = basketTestDataCreator.create()
        for (i in 1..50) {
            val productId = productTestDataCreator.create()
            val setBasketItemCommand =
                SetBasketItemCommand(
                    id = basketId,
                    productId = productId,
                    basketItemQuantity = BasketItemQuantity(1),
                )

            commandGateway.send<Any>(setBasketItemCommand)
        }
        val productId = productTestDataCreator.create()
        val setBasketItemCommand =
            SetBasketItemCommand(
                id = basketId,
                productId = productId,
                basketItemQuantity = BasketItemQuantity(1),
            )

        // Act
        val exception =
            assertThrows<CommandExecutionException> {
                commandGateway.sendAndWait<Any>(setBasketItemCommand)
            }

        // Assert
        assert(exception.isWrapUseCaseError())
        assert(exception.getWrapUseCaseError().errorCode == SetBasketItemError.PRODUCT_MAX_KIND_OVER.errorCode)
    }
}
