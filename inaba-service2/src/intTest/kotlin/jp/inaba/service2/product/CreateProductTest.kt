package jp.inaba.service2.product

import jp.inaba.core.domain.brand.BrandId
import jp.inaba.core.domain.brand.BrandName
import jp.inaba.core.domain.product.ProductDescription
import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.product.ProductName
import jp.inaba.core.domain.product.ProductPrice
import jp.inaba.core.domain.product.StockQuantity
import jp.inaba.message.brand.command.CreateBrandCommand
import jp.inaba.message.product.command.CreateProductCommand
import jp.inaba.message.product.command.CreateProductResult
import jp.inaba.service2.InabaIntegrationTestBase
import org.junit.jupiter.api.Test

class CreateProductTest : InabaIntegrationTestBase() {
  @Test
  fun `正常に商品を作成できる`() {
    val brandId = BrandId()
    val productId = ProductId()

    fixture.given()
      .command(CreateBrandCommand(id = brandId, name = BrandName("テストブランド")))
      .`when`()
      .command(
        CreateProductCommand(
          id = productId,
          brandId = brandId,
          name = ProductName("テスト商品"),
          description = ProductDescription("テスト商品の説明"),
          imageUrl = null,
          price = ProductPrice(1000),
          quantity = StockQuantity(10),
        )
      )
      .then()
      .success()
  }

  @Test
  fun `ブランドが存在しない場合は商品を作成できない`() {
    val nonExistentBrandId = BrandId()
    val productId = ProductId()

    fixture.given()
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
        )
      )
      .then()
      .resultMessagePayloadSatisfies(
        CreateProductResult::class.java
      ){ result ->
        result.error!!.message == CreateProductResult.brandNotFound().error!!.message
      }
  }
}
