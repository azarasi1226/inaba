package jp.inaba.service.domain.product

import jp.inaba.core.domain.brand.BrandId
import jp.inaba.core.domain.common.IdempotencyId
import jp.inaba.core.domain.product.DecreaseStockQuantity
import jp.inaba.core.domain.product.IncreaseStockQuantity
import jp.inaba.core.domain.product.ProductDescription
import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.product.ProductImageURL
import jp.inaba.core.domain.product.ProductName
import jp.inaba.core.domain.product.ProductPrice
import jp.inaba.core.domain.product.StockQuantity
import jp.inaba.message.product.command.DecreaseStockCommand
import jp.inaba.message.product.command.IncreaseStockCommand
import jp.inaba.message.product.command.UpdateProductCommand
import jp.inaba.message.product.event.ProductCreatedEvent
import jp.inaba.message.product.event.ProductUpdatedEvent
import jp.inaba.message.product.event.StockDecreasedEvent
import jp.inaba.message.product.event.StockIncreasedEvent
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ProductAggregateTest {
    private lateinit var fixture: FixtureConfiguration<ProductAggregate>

    @BeforeEach
    fun before() {
        fixture = AggregateTestFixture(ProductAggregate::class.java)
    }

    @Test
    fun `商品を作成_Event発行`() {
        // Arrange
        val productId = ProductId()
        val brandId = BrandId()
        val name = ProductName("おいしい昆布茶")
        val description = ProductDescription("健康になっちゃうかも")
        val imageUrl = ProductImageURL("http://amazon.s3/hoge.png")
        val price = ProductPrice(132)
        val quantity = StockQuantity(100)

        fixture
            .givenNoPriorActivity()
            // Act
            .`when`(
                InternalCreateProductCommand(
                    id = productId,
                    brandId = brandId,
                    name = name,
                    description = description,
                    imageUrl = imageUrl,
                    price = price,
                    quantity = quantity,
                ),
            )
            // Assert
            .expectSuccessfulHandlerExecution()
            .expectEvents(
                ProductCreatedEvent(
                    id = productId.value,
                    brandId = brandId.value,
                    name = name.value,
                    description = description.value,
                    imageUrl = imageUrl.value,
                    price = price.value,
                    quantity = quantity.value,
                ),
            )
    }

    @Test
    fun `商品を編集_Event発行`() {
        // Arrange
        // Create
        val productId = ProductId()
        val brandId = BrandId()
        val name = ProductName("おいしい昆布茶")
        val description = ProductDescription("健康になっちゃうかも")
        val imageUrl = ProductImageURL("http://amazon.s3/hoge.png")
        val price = ProductPrice(132)
        val quantity = StockQuantity(100)

        // Update
        val updatedName = ProductName("おいしすぎる昆布茶")
        val updatedPrice = ProductPrice(300)

        fixture
            .given(
                ProductCreatedEvent(
                    id = productId.value,
                    brandId = brandId.value,
                    name = name.value,
                    description = description.value,
                    imageUrl = imageUrl.value,
                    price = price.value,
                    quantity = quantity.value,
                ),
            )
            // Act
            .`when`(
                UpdateProductCommand(
                    id = productId,
                    name = updatedName,
                    description = description,
                    imageUrl = imageUrl,
                    price = updatedPrice,
                ),
            )
            // Assert
            .expectSuccessfulHandlerExecution()
            .expectEvents(
                ProductUpdatedEvent(
                    id = productId.value,
                    name = updatedName.value,
                    description = description.value,
                    imageUrl = imageUrl.value,
                    price = updatedPrice.value,
                ),
            )
    }

    @Test
    fun `在庫を入庫する_Event発行`() {
        // Arrange
        // Create
        val productId = ProductId()
        val brandId = BrandId()
        val name = ProductName("おいしい昆布茶")
        val description = ProductDescription("健康になっちゃうかも")
        val imageUrl = ProductImageURL("http://amazon.s3/hoge.png")
        val price = ProductPrice(132)
        val quantity = StockQuantity(100)

        val idempotencyId = IdempotencyId()
        val increaseStockQuantity = IncreaseStockQuantity(13)

        fixture
            .given(
                ProductCreatedEvent(
                    id = productId.value,
                    brandId = brandId.value,
                    name = name.value,
                    description = description.value,
                    imageUrl = imageUrl.value,
                    price = price.value,
                    quantity = quantity.value,
                ),
            )
            // Act
            .`when`(
                IncreaseStockCommand(
                    id = productId,
                    idempotencyId = idempotencyId,
                    increaseStockQuantity = increaseStockQuantity,
                ),
            )
            // Assert
            .expectSuccessfulHandlerExecution()
            .expectEvents(
                StockIncreasedEvent(
                    id = productId.value,
                    idempotencyId = idempotencyId.value,
                    increaseStockQuantity = increaseStockQuantity.value,
                    increasedStockQuantity = increaseStockQuantity.value + quantity.value,
                ),
            )
    }

    @Test
    fun `在庫を出庫する_Event発行`() {
        // Arrange
        // Create
        val productId = ProductId()
        val brandId = BrandId()
        val name = ProductName("おいしい昆布茶")
        val description = ProductDescription("健康になっちゃうかも")
        val imageUrl = ProductImageURL("http://amazon.s3/hoge.png")
        val price = ProductPrice(132)
        val quantity = StockQuantity(100)

        val idempotencyId = IdempotencyId()
        val decreaseStockQuantity = DecreaseStockQuantity(13)

        fixture
            .given(
                ProductCreatedEvent(
                    id = productId.value,
                    brandId = brandId.value,
                    name = name.value,
                    description = description.value,
                    imageUrl = imageUrl.value,
                    price = price.value,
                    quantity = quantity.value,
                ),
            )
            // Act
            .`when`(
                DecreaseStockCommand(
                    id = productId,
                    idempotencyId = idempotencyId,
                    decreaseStockQuantity = decreaseStockQuantity,
                ),
            )
            // Assert
            .expectSuccessfulHandlerExecution()
            .expectEvents(
                StockDecreasedEvent(
                    id = productId.value,
                    idempotencyId = idempotencyId.value,
                    decreaseStockQuantity = decreaseStockQuantity.value,
                    decreasedStockQuantity = quantity.value - decreaseStockQuantity.value,
                ),
            )
    }
}
