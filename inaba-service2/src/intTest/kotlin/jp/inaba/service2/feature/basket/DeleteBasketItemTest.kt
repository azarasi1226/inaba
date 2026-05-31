package jp.inaba.service2.feature.basket

import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.user.UserId
import jp.inaba.message.basket.command.DeleteBasketItemCommand
import jp.inaba.message.basket.command.DeleteBasketItemResult
import jp.inaba.message.basket.event.BasketItemDeletedEvent
import jp.inaba.message.basket.event.BasketItemSetEvent
import jp.inaba.message.user.event.UserCreatedEvent
import jp.inaba.service2.feature.InabaIntegrationTestBase
import org.junit.jupiter.api.Test

class DeleteBasketItemTest : InabaIntegrationTestBase() {
    @Test
    fun `正常系`() {
        val userId = UserId()
        val productId = ProductId()

        fixture
            .given()
            .events(
                UserCreatedEvent(
                    id = userId.value,
                    subject = "test-subject",
                ),
                BasketItemSetEvent(
                    userId = userId.value,
                    productId = productId.value,
                    basketItemQuantity = 1,
                ),
            ).`when`()
            .command(
                DeleteBasketItemCommand(
                    id = userId,
                    productId = productId,
                ),
            ).then()
            .resultMessagePayload(DeleteBasketItemResult.success())
            .events(
                BasketItemDeletedEvent(
                    userId = userId.value,
                    productId = productId.value,
                ),
            )
    }

    @Test
    fun `買い物かごに該当商品が存在しない場合_イベントなしで成功`() {
        val userId = UserId()
        val productId = ProductId()

        fixture
            .given()
            .events(
                UserCreatedEvent(
                    id = userId.value,
                    subject = "test-subject",
                ),
            ).`when`()
            .command(
                DeleteBasketItemCommand(
                    id = userId,
                    productId = productId,
                ),
            ).then()
            .resultMessagePayload(DeleteBasketItemResult.success())
            .noEvents()
    }

    @Test
    fun `ユーザーが存在しない_userNotFound`() {
        val userId = UserId()
        val productId = ProductId()

        fixture
            .given()
            .noPriorActivity()
            .`when`()
            .command(
                DeleteBasketItemCommand(
                    id = userId,
                    productId = productId,
                ),
            ).then()
            .resultMessagePayload(DeleteBasketItemResult.userNotFound())
            .noEvents()
    }
}
