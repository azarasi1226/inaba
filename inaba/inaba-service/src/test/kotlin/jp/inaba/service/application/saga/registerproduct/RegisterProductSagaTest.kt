package jp.inaba.service.application.saga.registerproduct

import com.github.michaelbull.result.Err
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.stock.CreateStockError
import jp.inaba.core.domain.stock.StockId
import jp.inaba.core.domain.stock.StockIdFactory
import jp.inaba.message.product.command.DeleteProductCommand
import jp.inaba.message.product.event.ProductCreatedEvent
import jp.inaba.message.stock.command.CreateStockCommand
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.test.saga.SagaTestFixture
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RegisterProductSagaTest {
    private lateinit var fixture: SagaTestFixture<RegisterProductSaga>

    @MockK
    private lateinit var stockIdFactory: StockIdFactory

    @MockK
    private lateinit var commandGateway: CommandGateway

    val stockId = StockId()

    @BeforeEach
    fun before() {
        MockKAnnotations.init(this)

        every {
            stockIdFactory.handle()
        } returns stockId
        every {
            commandGateway.sendAndWait<ActionCommandResult>(any())
        } returns ActionCommandResult.ok()

        fixture = SagaTestFixture(RegisterProductSaga::class.java)
        fixture.withTransienceCheckDisabled()
        fixture.registerResource(stockIdFactory)
        fixture.registerCommandGateway(CommandGateway::class.java, commandGateway)
    }

    @Test
    fun `商品が作られた_在庫を作成する_在庫作成コマンド発行`() {
        val productId = ProductId()
        val productCreatedEvent = ProductCreatedEvent(
            id = productId.value,
            name = "",
            description = "",
            imageUrl = "",
            price = 0
        )

        fixture.givenNoPriorActivity()
            .whenPublishingA(productCreatedEvent)
            .expectActiveSagas(1)
            .expectDispatchedCommands(
                CreateStockCommand(
                    id = stockId,
                    productId = productId
                )
            )
    }

    @Test
    fun `商品が作られた_在庫を作成できなかった_商品削除コマンド発行`() {
        val productId = ProductId()
        val productCreatedEvent = ProductCreatedEvent(
            id = productId.value,
            name = "",
            description = "",
            imageUrl = "",
            price = 0
        )

        every {
            commandGateway.sendAndWait<Any>(any())
        } returns Err(CreateStockError.ProductNotExits)

        fixture.givenNoPriorActivity()
            .whenPublishingA(productCreatedEvent)
            .expectActiveSagas(1)
            .expectDispatchedCommands(
                DeleteProductCommand(
                    id = productId
                )
            )
    }
}