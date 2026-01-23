package jp.inaba.service.basket

import jp.inaba.core.domain.basket.BasketId
import jp.inaba.core.domain.basket.BasketItemQuantity
import jp.inaba.core.domain.brand.BrandId
import jp.inaba.core.domain.brand.BrandName
import jp.inaba.core.domain.common.PagingCondition
import jp.inaba.core.domain.product.*
import jp.inaba.core.domain.user.UserId
import jp.inaba.message.basket.command.SetBasketItemCommand
import jp.inaba.message.basket.query.FindBasketByIdQuery
import jp.inaba.message.basket.query.FindBasketByIdResult
import jp.inaba.message.brand.command.CreateBrandCommand
import jp.inaba.message.product.command.CreateProductCommand
import jp.inaba.message.user.command.CreateUserCommand
import jp.inaba.message.user.query.FindUserMetadataBySubjectQuery
import jp.inaba.message.user.query.FindUserMetadataBySubjectResult
import jp.inaba.service.MySqlTestContainerFactory
import jp.inaba.service.utlis.retryQuery
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.queryhandling.QueryGateway
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@ActiveProfiles("integration-test")
@Testcontainers
class SetBasketItemIntegrationTest  {
  companion object {
    @Container
    @ServiceConnection
    val mysql = MySqlTestContainerFactory.create()
  }

  @Autowired
  lateinit var commandGateway: CommandGateway

  @Autowired
  lateinit var queryGateway: QueryGateway

  @Test
  fun `バスケットにアイテムを追加する_成功する`() {
    // Arrange
    // Basket作成
    val createUserCommand =
      CreateUserCommand(
        id = UserId(),
        subject = "aaa",
      )
    commandGateway.sendAndWait<Any>(createUserCommand)
    val request = FindUserMetadataBySubjectQuery(createUserCommand.subject)
    // 結果整合性により、すぐ問い合わせてもまだプロジェクションされて無い可能性があるのでリトライする
    val result = queryGateway.retryQuery<FindUserMetadataBySubjectResult, FindUserMetadataBySubjectQuery>(request)

    val createBrandCommand = CreateBrandCommand(
      id = BrandId(),
      name = BrandName("伊藤園"),
    )

    commandGateway.sendAndWait<Any>(createBrandCommand)

    Thread.sleep(500)
    // Product作成
    val productId = ProductId()
    val brandId = createBrandCommand.id
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
    commandGateway.sendAndWait<Any>(command)

    // Act
    val setBasketItemCommand =
      SetBasketItemCommand(
        id = BasketId(result.basketId),
        productId = productId,
        basketItemQuantity = BasketItemQuantity(3),
      )
    Thread.sleep(500)
    commandGateway.sendAndWait<Any>(setBasketItemCommand)

    // Assert
    val query = FindBasketByIdQuery(
      basketId = BasketId(result.basketId),
      pagingCondition = PagingCondition(
        pageNumber = 1,
        pageSize = 10,
      )
    )

    //まだできてない可能性があるので、いったんスレッドスリープ
    Thread.sleep(500)
    val basketResult = queryGateway.retryQuery<FindBasketByIdResult, FindBasketByIdQuery>(query)
    assert(basketResult.page.items.size == 1)
    val basketItem = basketResult.page.items[0]
    assert(basketItem.productId == productId.value)
    assert(basketItem.basketItemQuantity == 3)
  }
}