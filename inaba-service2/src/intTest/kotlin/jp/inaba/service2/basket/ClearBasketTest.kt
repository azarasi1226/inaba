package jp.inaba.service2.basket

import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.user.UserId
import jp.inaba.message.basket.command.ClearBasketCommand
import jp.inaba.message.basket.command.ClearBasketResult
import jp.inaba.message.basket.event.BasketClearedEvent
import jp.inaba.message.basket.event.BasketItemSetEvent
import jp.inaba.service2.InabaIntegrationTestBase
import org.junit.jupiter.api.Test

class ClearBasketTest : InabaIntegrationTestBase() {
    @Test
    fun `正常系_買い物かごにアイテムがある`() {
        val userId = UserId()
        val productId = ProductId()

        fixture
            .given()
            .events(
                BasketItemSetEvent(
                    userId = userId.value,
                    productId = productId.value,
                    basketItemQuantity = 1,
                ),
            ).`when`()
            .command(
                ClearBasketCommand(
                    id = userId,
                ),
            ).then()
            .resultMessagePayload(ClearBasketResult.success())
            .events(
                BasketClearedEvent(
                    userId = userId.value,
                ),
            )
    }

    @Test
    fun `買い物かごが空の場合_イベントなしで成功`() {
        val userId = UserId()

        fixture
            .given()
            .noPriorActivity()
            .`when`()
            .command(
                ClearBasketCommand(
                    id = userId,
                ),
            ).then()
            .resultMessagePayload(ClearBasketResult.success())
            .noEvents()
    }
}