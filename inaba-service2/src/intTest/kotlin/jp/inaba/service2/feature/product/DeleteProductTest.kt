package jp.inaba.service2.feature.product

import jp.inaba.core.domain.brand.BrandId
import jp.inaba.core.domain.product.ProductId
import jp.inaba.message.brand.event.BrandCreatedEvent
import jp.inaba.message.product.command.DeleteProductCommand
import jp.inaba.message.product.command.DeleteProductResult
import jp.inaba.message.product.event.ProductCreatedEvent
import jp.inaba.message.product.event.ProductDeletedEvent
import jp.inaba.service2.feature.InabaIntegrationTestBase
import org.junit.jupiter.api.Test

class DeleteProductTest : InabaIntegrationTestBase() {
    @Test
    fun `正常系_商品削除成功`() {
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
                DeleteProductCommand(
                    id = productId,
                ),
            ).then()
            .resultMessagePayload(DeleteProductResult.success())
            .events(
                ProductDeletedEvent(
                    id = productId.value,
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
                DeleteProductCommand(
                    id = productId,
                ),
            ).then()
            .resultMessagePayload(DeleteProductResult.notFound())
            .noEvents()
    }

    @Test
    fun `すでに削除済みの場合_冪等性により成功を返す`() {
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
                DeleteProductCommand(
                    id = productId,
                ),
            ).then()
            .resultMessagePayload(DeleteProductResult.success())
            .noEvents()
    }
}
