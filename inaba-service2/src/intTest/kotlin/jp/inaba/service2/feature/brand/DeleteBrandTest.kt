package jp.inaba.service2.feature.brand

import jp.inaba.core.domain.brand.BrandId
import jp.inaba.message.brand.command.DeleteBrandCommand
import jp.inaba.message.brand.command.DeleteBrandResult
import jp.inaba.message.brand.event.BrandCreatedEvent
import jp.inaba.message.brand.event.BrandDeletedEvent
import jp.inaba.message.product.event.ProductCreatedEvent
import jp.inaba.message.product.event.ProductDeletedEvent
import jp.inaba.service2.feature.InabaIntegrationTestBase
import org.junit.jupiter.api.Test

class DeleteBrandTest : InabaIntegrationTestBase() {
    @Test
    fun `正常系`() {
        val brandId = BrandId()
        val command = DeleteBrandCommand(id = brandId)

        fixture
            .given()
            .events(
                BrandCreatedEvent(
                    id = brandId.value,
                    name = "テストブランド",
                ),
            )
            .`when`()
            .command(command)
            .then()
            .resultMessagePayload(DeleteBrandResult.success())
            .events(
                BrandDeletedEvent(
                    id = brandId.value,
                ),
            )
    }

    @Test
    fun `ブランドが存在しない場合_notFound`() {
        val command = DeleteBrandCommand(id = BrandId())

        fixture
            .given()
            .noPriorActivity()
            .`when`()
            .command(command)
            .then()
            .resultMessagePayload(DeleteBrandResult.notFound())
            .noEvents()
    }

    @Test
    fun `ブランドに紐づく商品が存在する場合_hasLinkedProducts`() {
        val brandId = BrandId()
        val command = DeleteBrandCommand(id = brandId)

        fixture
            .given()
            .events(
                BrandCreatedEvent(
                    id = brandId.value,
                    name = "テストブランド",
                ),
                ProductCreatedEvent(
                    id = "product-1",
                    brandId = brandId.value,
                    name = "テスト商品",
                    description = "テスト説明",
                    imageUrl = null,
                    price = 1000,
                    quantity = 10,
                ),
            )
            .`when`()
            .command(command)
            .then()
            .resultMessagePayload(DeleteBrandResult.hasLinkedProducts())
            .noEvents()
    }

    @Test
    fun `すでに削除済みの場合_冪等性により成功を返す`() {
        val brandId = BrandId()
        val command = DeleteBrandCommand(id = brandId)

        fixture
            .given()
            .events(
                BrandCreatedEvent(
                    id = brandId.value,
                    name = "テストブランド",
                ),
                BrandDeletedEvent(
                    id = brandId.value,
                ),
            )
            .`when`()
            .command(command)
            .then()
            .resultMessagePayload(DeleteBrandResult.success())
            .noEvents()
    }

    @Test
    fun `商品が作成後に削除された場合_ブランド削除に成功する`() {
        val brandId = BrandId()
        val command = DeleteBrandCommand(id = brandId)

        fixture
            .given()
            .events(
                BrandCreatedEvent(
                    id = brandId.value,
                    name = "テストブランド",
                ),
                ProductCreatedEvent(
                    id = "product-1",
                    brandId = brandId.value,
                    name = "テスト商品",
                    description = "テスト説明",
                    imageUrl = null,
                    price = 1000,
                    quantity = 10,
                ),
                ProductDeletedEvent(
                    id = "product-1",
                ),
            )
            .`when`()
            .command(command)
            .then()
            .resultMessagePayload(DeleteBrandResult.success())
            .events(
                BrandDeletedEvent(
                    id = brandId.value,
                ),
            )
    }
}
