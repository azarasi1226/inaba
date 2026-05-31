package jp.inaba.service2.feature.product

import jp.inaba.core.domain.brand.BrandId
import jp.inaba.core.domain.product.ProductDescription
import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.product.ProductImageURL
import jp.inaba.core.domain.product.ProductName
import jp.inaba.core.domain.product.ProductPrice
import jp.inaba.message.brand.event.BrandCreatedEvent
import jp.inaba.message.product.command.UpdateProductCommand
import jp.inaba.message.product.command.UpdateProductResult
import jp.inaba.message.product.event.ProductCreatedEvent
import jp.inaba.message.product.event.ProductDeletedEvent
import jp.inaba.message.product.event.ProductUpdatedEvent
import jp.inaba.service2.feature.InabaIntegrationTestBase
import org.junit.jupiter.api.Test

class UpdateProductTest : InabaIntegrationTestBase() {
    @Test
    fun `正常系_商品更新成功`() {
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
            ).`when`()
            .command(
                UpdateProductCommand(
                    id = productId,
                    name = ProductName("更新後の商品名"),
                    description = ProductDescription("更新後の説明"),
                    imageUrl = ProductImageURL("https://example.com/image.png"),
                    price = ProductPrice(2000),
                ),
            ).then()
            .resultMessagePayload(UpdateProductResult.success())
            .events(
                ProductUpdatedEvent(
                    id = productId.value,
                    name = "更新後の商品名",
                    description = "更新後の説明",
                    imageUrl = "https://example.com/image.png",
                    price = 2000,
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
                UpdateProductCommand(
                    id = productId,
                    name = ProductName("更新後の商品名"),
                    description = ProductDescription("更新後の説明"),
                    imageUrl = null,
                    price = ProductPrice(2000),
                ),
            ).then()
            .resultMessagePayload(UpdateProductResult.notFound())
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
                UpdateProductCommand(
                    id = productId,
                    name = ProductName("更新後の商品名"),
                    description = ProductDescription("更新後の説明"),
                    imageUrl = null,
                    price = ProductPrice(2000),
                ),
            ).then()
            .resultMessagePayload(UpdateProductResult.deleted())
            .noEvents()
    }
}
