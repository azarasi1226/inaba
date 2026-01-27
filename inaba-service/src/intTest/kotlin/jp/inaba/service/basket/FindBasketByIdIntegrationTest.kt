package jp.inaba.service.basket

import jp.inaba.core.domain.basket.BasketItemQuantity
import jp.inaba.core.domain.common.PagingCondition
import jp.inaba.core.domain.product.ProductName
import jp.inaba.message.basket.command.SetBasketItemCommand
import jp.inaba.message.basket.query.FindBasketByIdQuery
import jp.inaba.message.basket.query.FindBasketByIdResult
import jp.inaba.service.MySqlTestContainerFactory
import jp.inaba.service.fixture.BasketTestDataCreator
import jp.inaba.service.fixture.ProductTestDataCreator
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.extensions.kotlin.query
import org.axonframework.queryhandling.QueryGateway
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@ActiveProfiles("integration-test")
@Testcontainers
class FindBasketByIdIntegrationTest(
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
        val ids =
            listOf(
                productTestDataCreator.create(name = ProductName("おーいお茶")),
                productTestDataCreator.create(name = ProductName("キリンレモン")),
            )
        ids.forEach {
            val setBasketItemCommand =
                SetBasketItemCommand(
                    id = basketId,
                    productId = it,
                    basketItemQuantity = BasketItemQuantity(1),
                )
            commandGateway.sendAndWait<Any>(setBasketItemCommand)
        }
        val query = FindBasketByIdQuery(basketId, pagingCondition = PagingCondition(pageSize = 10, pageNumber = 1))

        // Act
        val result = queryGateway.query<FindBasketByIdResult, FindBasketByIdQuery>(query).get()

        // Assert
        // Projectionが結果整合性のために遅延する可能性がある。少し待つ
        Thread.sleep(100)
        assert(result.page.items.size == 2)
        assert(result.page.paging.totalCount == 2L)
        assert(result.page.paging.pageSize == 10)
        assert(result.page.paging.pageNumber == 1)
        assert(result.page.paging.totalPage == 1)
        assert(result.page.items.any { it.productName == "おーいお茶" })
        assert(result.page.items.any { it.productName == "キリンレモン" })
    }
}
