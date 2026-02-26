package jp.inaba.service2.feature.basket

import jp.inaba.core.domain.basket.BasketItemQuantity
import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.user.UserId
import jp.inaba.message.basket.command.SetBasketItemCommand
import jp.inaba.message.basket.command.SetBasketItemResult
import jp.inaba.message.basket.event.BasketItemSetEvent
import jp.inaba.message.product.event.ProductCreatedEvent
import jp.inaba.message.user.event.UserCreatedEvent
import jp.inaba.service2.feature.InabaIntegrationTestBase
import org.junit.jupiter.api.Test

class SetBasketItemTest : InabaIntegrationTestBase() {
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
                ProductCreatedEvent(
                    id = productId.value,
                    brandId = "brand-id",
                    name = "テスト商品",
                    description = "テスト商品の説明",
                    imageUrl = null,
                    price = 1000,
                    quantity = 10,
                ),
            ).`when`()
            .command(
                SetBasketItemCommand(
                    id = userId,
                    productId = productId,
                    basketItemQuantity = BasketItemQuantity(1),
                ),
            ).then()
            .resultMessagePayload(SetBasketItemResult.success())
            .events(
                BasketItemSetEvent(
                    userId = userId.value,
                    productId = productId.value,
                    basketItemQuantity = 1,
                ),
            )
    }

    @Test
    fun `ユーザーが存在しない_userNotFound`() {
        val userId = UserId()
        val productId = ProductId()

        fixture
            .given()
            .events(
                ProductCreatedEvent(
                    id = productId.value,
                    brandId = "brand-id",
                    name = "テスト商品",
                    description = "テスト商品の説明",
                    imageUrl = null,
                    price = 1000,
                    quantity = 10,
                ),
            ).`when`()
            .command(
                SetBasketItemCommand(
                    id = userId,
                    productId = productId,
                    basketItemQuantity = BasketItemQuantity(1),
                ),
            ).then()
            .resultMessagePayload(SetBasketItemResult.userNotFound())
            .noEvents()
    }

    @Test
    fun `商品が存在しない_productNotFound`() {
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
                SetBasketItemCommand(
                    id = userId,
                    productId = productId,
                    basketItemQuantity = BasketItemQuantity(1),
                ),
            ).then()
            .resultMessagePayload(SetBasketItemResult.productNotFound())
            .noEvents()
    }

    @Test
    fun `商品種類の上限超過_productMaxKindOver`() {
        val userId = UserId()
        val productId = ProductId()

        val existingItems = (1..50).map { i ->
            val existingProductId = ProductId()
            listOf(
                ProductCreatedEvent(
                    id = existingProductId.value,
                    brandId = "brand-id",
                    name = "テスト商品$i",
                    description = "テスト商品${i}の説明",
                    imageUrl = null,
                    price = 1000,
                    quantity = 10,
                ),
                BasketItemSetEvent(
                    userId = userId.value,
                    productId = existingProductId.value,
                    basketItemQuantity = 1,
                ),
            )
        }.flatten()

        fixture
            .given()
            .events(
                listOf(
                    UserCreatedEvent(
                        id = userId.value,
                        subject = "test-subject",
                    ),
                    ProductCreatedEvent(
                        id = productId.value,
                        brandId = "brand-id",
                        name = "新しいテスト商品",
                        description = "新しいテスト商品の説明",
                        imageUrl = null,
                        price = 1000,
                        quantity = 10,
                    ),
                ) + existingItems,
            ).`when`()
            .command(
                SetBasketItemCommand(
                    id = userId,
                    productId = productId,
                    basketItemQuantity = BasketItemQuantity(1),
                ),
            ).then()
            .resultMessagePayload(SetBasketItemResult.productMaxKindOver())
            .noEvents()
    }
}