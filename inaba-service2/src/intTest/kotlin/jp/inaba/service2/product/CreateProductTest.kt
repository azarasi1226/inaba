package jp.inaba.service2.product

import jp.inaba.core.domain.brand.BrandId
import jp.inaba.core.domain.product.ProductDescription
import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.product.ProductName
import jp.inaba.core.domain.product.ProductPrice
import jp.inaba.core.domain.product.StockQuantity
import jp.inaba.message.brand.event.BrandCreatedEvent
import jp.inaba.message.product.command.CreateProductCommand
import jp.inaba.message.product.command.CreateProductResult
import jp.inaba.message.product.event.ProductCreatedEvent
import jp.inaba.service2.InabaIntegrationTestBase
import org.junit.jupiter.api.Test

class CreateProductTest : InabaIntegrationTestBase() {
    @Test
    fun `正常系`() {
        val productId = ProductId()
        val brandId = BrandId()

        fixture
            .given()
            .event(
                BrandCreatedEvent(
                    id = brandId.value,
                    name = "テストブランド",
                ),
            ).`when`()
            .command(
                CreateProductCommand(
                    id = productId,
                    brandId = brandId,
                    name = ProductName("テスト商品"),
                    description = ProductDescription("テスト商品の説明"),
                    imageUrl = null,
                    price = ProductPrice(1000),
                    quantity = StockQuantity(10),
                ),
            ).then()
            .resultMessagePayload(CreateProductResult.success())
            .events(
                ProductCreatedEvent(
                    id = productId.value,
                    brandId = brandId.value,
                    name = "テスト商品",
                    description = "テスト商品の説明",
                    imageUrl = null,
                    price = 1000,
                    quantity = 10,
                ),
            )
    }

    @Test
    fun `ブランドが存在しない_brandNotFound`() {
        val nonExistentBrandId = BrandId()
        val productId = ProductId()

        fixture
            .given()
            .noPriorActivity()
            .`when`()
            .command(
                CreateProductCommand(
                    id = productId,
                    brandId = nonExistentBrandId,
                    name = ProductName("テスト商品"),
                    description = ProductDescription("テスト商品の説明"),
                    imageUrl = null,
                    price = ProductPrice(1000),
                    quantity = StockQuantity(10),
                ),
            ).then()
            .resultMessagePayload(CreateProductResult.brandNotFound())
    }

    @Test
    fun `すでに同じIDで商品が登録済み_alreadyExists`() {
        val existsProductId = ProductId()
        val brandId = BrandId()

        fixture
            .given()
            .events(
                BrandCreatedEvent(
                    id = brandId.value,
                    name = "テストブランド",
                ),
                ProductCreatedEvent(
                    id = existsProductId.value,
                    brandId = brandId.value,
                    name = "テスト商品",
                    description = "テスト商品の説明",
                    imageUrl = null,
                    price = 1000,
                    quantity = 10,
                ),
            ).`when`()
            .command(
                CreateProductCommand(
                    id = existsProductId,
                    brandId = brandId,
                    name = ProductName("テスト商品2"),
                    description = ProductDescription("テスト商品2の説明"),
                    imageUrl = null,
                    price = ProductPrice(1000),
                    quantity = StockQuantity(10),
                ),
            ).then()
            .resultMessagePayload(CreateProductResult.alreadyExists())
    }
}
