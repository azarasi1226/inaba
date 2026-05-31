package jp.inaba.service2.feature.product

import jp.inaba.core.domain.brand.BrandId
import jp.inaba.core.domain.common.IdempotencyId
import jp.inaba.core.domain.product.DecreaseStockQuantity
import jp.inaba.core.domain.product.ProductId
import jp.inaba.message.brand.event.BrandCreatedEvent
import jp.inaba.message.product.command.DecreaseStockCommand
import jp.inaba.message.product.command.DecreaseStockResult
import jp.inaba.message.product.event.ProductCreatedEvent
import jp.inaba.message.product.event.ProductDeletedEvent
import jp.inaba.message.product.event.StockDecreasedEvent
import jp.inaba.service2.feature.InabaIntegrationTestBase
import org.junit.jupiter.api.Test

class DecreaseStockTest : InabaIntegrationTestBase() {
    @Test
    fun `正常系_在庫減少成功`() {
        val productId = ProductId()
        val brandId = BrandId()
        val idempotencyId = IdempotencyId()

        fixture
            .given()
            .events(
                BrandCreatedEvent(
                    id = brandId.value,
                    name = "テストブランド",
                ),
                ProductCreatedEvent(
                    id = productId.value,
                    brandId = brandId.value,
                    name = "テスト商品",
                    description = "テスト商品の説明",
                    imageUrl = null,
                    price = 1000,
                    quantity = 10,
                ),
            ).`when`()
            .command(
                DecreaseStockCommand(
                    id = productId,
                    idempotencyId = idempotencyId,
                    decreaseStockQuantity = DecreaseStockQuantity(3),
                ),
            ).then()
            .resultMessagePayload(DecreaseStockResult.success())
            .events(
                StockDecreasedEvent(
                    id = productId.value,
                    idempotencyId = idempotencyId.value,
                    decreaseStockQuantity = 3,
                    decreasedStockQuantity = 7,
                ),
            )
    }

    @Test
    fun `商品が存在しない場合_notFound`() {
        val productId = ProductId()

        fixture
            .given()
            .noPriorActivity()
            .`when`()
            .command(
                DecreaseStockCommand(
                    id = productId,
                    idempotencyId = IdempotencyId(),
                    decreaseStockQuantity = DecreaseStockQuantity(1),
                ),
            ).then()
            .resultMessagePayload(DecreaseStockResult.notFound())
            .noEvents()
    }

    @Test
    fun `商品が削除済みの場合_deleted`() {
        val productId = ProductId()
        val brandId = BrandId()

        fixture
            .given()
            .events(
                BrandCreatedEvent(
                    id = brandId.value,
                    name = "テストブランド",
                ),
                ProductCreatedEvent(
                    id = productId.value,
                    brandId = brandId.value,
                    name = "テスト商品",
                    description = "テスト商品の説明",
                    imageUrl = null,
                    price = 1000,
                    quantity = 10,
                ),
                ProductDeletedEvent(
                    id = productId.value,
                ),
            ).`when`()
            .command(
                DecreaseStockCommand(
                    id = productId,
                    idempotencyId = IdempotencyId(),
                    decreaseStockQuantity = DecreaseStockQuantity(1),
                ),
            ).then()
            .resultMessagePayload(DecreaseStockResult.deleted())
            .noEvents()
    }

    @Test
    fun `在庫不足の場合_insufficientStock`() {
        val productId = ProductId()
        val brandId = BrandId()

        fixture
            .given()
            .events(
                BrandCreatedEvent(
                    id = brandId.value,
                    name = "テストブランド",
                ),
                ProductCreatedEvent(
                    id = productId.value,
                    brandId = brandId.value,
                    name = "テスト商品",
                    description = "テスト商品の説明",
                    imageUrl = null,
                    price = 1000,
                    quantity = 5,
                ),
            ).`when`()
            .command(
                DecreaseStockCommand(
                    id = productId,
                    idempotencyId = IdempotencyId(),
                    decreaseStockQuantity = DecreaseStockQuantity(10),
                ),
            ).then()
            .resultMessagePayload(DecreaseStockResult.insufficientStock())
            .noEvents()
    }

    @Test
    fun `冪等性_同じidempotencyIdで2回目は成功しイベントなし`() {
        val productId = ProductId()
        val brandId = BrandId()
        val idempotencyId = IdempotencyId()

        fixture
            .given()
            .events(
                BrandCreatedEvent(
                    id = brandId.value,
                    name = "テストブランド",
                ),
                ProductCreatedEvent(
                    id = productId.value,
                    brandId = brandId.value,
                    name = "テスト商品",
                    description = "テスト商品の説明",
                    imageUrl = null,
                    price = 1000,
                    quantity = 10,
                ),
                StockDecreasedEvent(
                    id = productId.value,
                    idempotencyId = idempotencyId.value,
                    decreaseStockQuantity = 3,
                    decreasedStockQuantity = 7,
                ),
            ).`when`()
            .command(
                DecreaseStockCommand(
                    id = productId,
                    idempotencyId = idempotencyId,
                    decreaseStockQuantity = DecreaseStockQuantity(3),
                ),
            ).then()
            .resultMessagePayload(DecreaseStockResult.success())
            .noEvents()
    }
}
