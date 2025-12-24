package jp.inaba.service.application.command.product

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import jp.inaba.core.domain.brand.BrandId
import jp.inaba.core.domain.common.CommonError
import jp.inaba.core.domain.common.UseCaseException
import jp.inaba.core.domain.product.CreateProductError
import jp.inaba.core.domain.product.ProductDescription
import jp.inaba.core.domain.product.ProductId
import jp.inaba.core.domain.product.ProductImageURL
import jp.inaba.core.domain.product.ProductName
import jp.inaba.core.domain.product.ProductPrice
import jp.inaba.core.domain.product.StockQuantity
import jp.inaba.message.product.command.CreateProductCommand
import jp.inaba.service.domain.UniqueAggregateIdVerifier
import jp.inaba.service.domain.product.CreateProductVerifier
import jp.inaba.service.domain.product.InternalCreateProductCommand
import org.axonframework.commandhandling.gateway.CommandGateway
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class CreateProductInteractorTest {
    @MockK
    private lateinit var uniqueAggregateIdVerifier: UniqueAggregateIdVerifier

    @MockK
    private lateinit var createProductVerifier: CreateProductVerifier

    @MockK
    private lateinit var commandGateway: CommandGateway

    @InjectMockKs
    private lateinit var sut: CreateProductInteractor

    @AfterEach
    fun after() {
        confirmVerified(commandGateway)
    }

    @Test
    fun `ブランドが存在_商品を作成_Command配送`() {
        // Arrange
        val productId = ProductId()
        val brandId = BrandId()
        val name = ProductName("おいしい昆布茶")
        val description = ProductDescription("健康になっちゃうかも")
        val imageUrl = ProductImageURL("http://amazon.s3/hoge.png")
        val price = ProductPrice(132)
        val quantity = StockQuantity(100)
        val command =
            CreateProductCommand(
                id = productId,
                brandId = brandId,
                name = name,
                description = description,
                imageUrl = imageUrl,
                price = price,
                quantity = quantity,
            )
        every {
            uniqueAggregateIdVerifier.hasDuplicateAggregateId(productId.value)
        } returns false
        every {
            createProductVerifier.isBrandNotFound(brandId)
        } returns false
        every {
            commandGateway.sendAndWait<Any>(any())
        } returns Unit

        // Act
        sut.handle(command)

        // Assert
        val expectCommand =
            InternalCreateProductCommand(
                id = productId,
                brandId = brandId,
                name = name,
                description = description,
                imageUrl = imageUrl,
                price = price,
                quantity = quantity,
            )
        verify(exactly = 1) {
            commandGateway.sendAndWait<Any>(expectCommand)
        }
    }

    @Test
    fun `登録しようとしているIDで集約が作成済み_商品を登録_Commandが配送されずException`() {
        // Arrange
        val productId = ProductId()
        val brandId = BrandId()
        val name = ProductName("おいしい昆布茶")
        val description = ProductDescription("健康になっちゃうかも")
        val imageUrl = ProductImageURL("http://amazon.s3/hoge.png")
        val price = ProductPrice(132)
        val quantity = StockQuantity(100)
        val command =
            CreateProductCommand(
                id = productId,
                brandId = brandId,
                name = name,
                description = description,
                imageUrl = imageUrl,
                price = price,
                quantity = quantity,
            )
        every {
            uniqueAggregateIdVerifier.hasDuplicateAggregateId(productId.value)
        } returns true

        // Act
        val exception =
            assertThrows<UseCaseException> {
                sut.handle(command)
            }

        // Assert
        assert(exception.error == CommonError.AGGREGATE_DUPLICATED)
    }

    @Test
    fun `ブランドが存在しない_商品を登録する_Commandが配送されずException`() {
        // Arrange
        val productId = ProductId()
        val brandId = BrandId()
        val name = ProductName("おいしい昆布茶")
        val description = ProductDescription("健康になっちゃうかも")
        val imageUrl = ProductImageURL("http://amazon.s3/hoge.png")
        val price = ProductPrice(132)
        val quantity = StockQuantity(100)
        val command =
            CreateProductCommand(
                id = productId,
                brandId = brandId,
                name = name,
                description = description,
                imageUrl = imageUrl,
                price = price,
                quantity = quantity,
            )
        every {
            uniqueAggregateIdVerifier.hasDuplicateAggregateId(productId.value)
        } returns false
        every {
            createProductVerifier.isBrandNotFound(brandId)
        } returns true

        // Act
        val exception =
            assertThrows<UseCaseException> {
                sut.handle(command)
            }

        // Assert
        assert(exception.error == CreateProductError.BRAND_NOT_FOUND)
    }
}
