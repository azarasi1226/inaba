package jp.inaba.service2.feature.product

import jp.inaba.core.domain.brand.BrandId
import jp.inaba.core.domain.common.IdempotencyId
import jp.inaba.core.domain.product.IncreaseStockQuantity
import jp.inaba.core.domain.product.ProductId
import jp.inaba.message.brand.event.BrandCreatedEvent
import jp.inaba.message.product.command.IncreaseStockCommand
import jp.inaba.message.product.command.IncreaseStockResult
import jp.inaba.message.product.event.ProductCreatedEvent
import jp.inaba.message.product.event.ProductDeletedEvent
import jp.inaba.message.product.event.StockIncreasedEvent
import jp.inaba.service2.feature.InabaIntegrationTestBase
import org.junit.jupiter.api.Test

class IncreaseStockTest : InabaIntegrationTestBase() {
    @Test
    fun `正常系_在庫増加成功`() {
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
                IncreaseStockCommand(
                    id = productId,
                    idempotencyId = idempotencyId,
                    increaseStockQuantity = IncreaseStockQuantity(5),
                ),
            ).then()
            .resultMessagePayload(IncreaseStockResult.success())
            .events(
                StockIncreasedEvent(
                    id = productId.value,
                    idempotencyId = idempotencyId.value,
                    increaseStockQuantity = 5,
                    increasedStockQuantity = 15,
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
                IncreaseStockCommand(
                    id = productId,
                    idempotencyId = IdempotencyId(),
                    increaseStockQuantity = IncreaseStockQuantity(1),
                ),
            ).then()
            .resultMessagePayload(IncreaseStockResult.notFound())
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
                IncreaseStockCommand(
                    id = productId,
                    idempotencyId = IdempotencyId(),
                    increaseStockQuantity = IncreaseStockQuantity(1),
                ),
            ).then()
            .resultMessagePayload(IncreaseStockResult.deleted())
            .noEvents()
    }

    @Test
    fun `在庫上限超過の場合_cannotReceive`() {
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
                    quantity = 999_999,
                ),
            ).`when`()
            .command(
                IncreaseStockCommand(
                    id = productId,
                    idempotencyId = IdempotencyId(),
                    increaseStockQuantity = IncreaseStockQuantity(2),
                ),
            ).then()
            .resultMessagePayload(IncreaseStockResult.cannotReceive())
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
                StockIncreasedEvent(
                    id = productId.value,
                    idempotencyId = idempotencyId.value,
                    increaseStockQuantity = 5,
                    increasedStockQuantity = 15,
                ),
            ).`when`()
            .command(
                IncreaseStockCommand(
                    id = productId,
                    idempotencyId = idempotencyId,
                    increaseStockQuantity = IncreaseStockQuantity(5),
                ),
            ).then()
            .resultMessagePayload(IncreaseStockResult.success())
            .noEvents()
    }
}
