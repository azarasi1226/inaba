// package jp.inaba.service.basket
//
// import jp.inaba.core.domain.basket.BasketId
// import jp.inaba.core.domain.basket.CreateBasketError
// import jp.inaba.core.domain.basket.SetBasketItemError
// import jp.inaba.core.domain.user.UserId
// import jp.inaba.message.basket.command.CreateBasketCommand
// import jp.inaba.service.MySqlTestContainerFactory
// import jp.inaba.service.fixture.UserTestDataCreator
// import jp.inaba.service.utlis.getWrapUseCaseError
// import jp.inaba.service.utlis.isWrapUseCaseError
// import org.axonframework.commandhandling.CommandExecutionException
// import org.axonframework.commandhandling.gateway.CommandGateway
// import org.junit.jupiter.api.Test
// import org.junit.jupiter.api.assertDoesNotThrow
// import org.junit.jupiter.api.assertThrows
// import org.springframework.beans.factory.annotation.Autowired
// import org.springframework.boot.test.context.SpringBootTest
// import org.springframework.boot.testcontainers.service.connection.ServiceConnection
// import org.springframework.test.context.ActiveProfiles
// import org.testcontainers.junit.jupiter.Container
// import org.testcontainers.junit.jupiter.Testcontainers
//
// @SpringBootTest
// @ActiveProfiles("integration-test")
// @Testcontainers
// class CreateBasketIntegrationTest(
//    @param:Autowired
//    private val commandGateway: CommandGateway,
// ) {
//    companion object {
//        @Container
//        @ServiceConnection
//        private val mysql = MySqlTestContainerFactory.create()
//    }
//
//  private val userTestDataCreator = UserTestDataCreator(commandGateway)
//
//    @Test
//    fun `買い物かごを作成する_成功`() {
//        // Arrange
//        val userId = userTestDataCreator.handle()
//        val createBasketCommand = CreateBasketCommand(
//            id = BasketId(),
//            userId = userId,
//        )
//
//        // Act & Assert
//        assertDoesNotThrow {
//            commandGateway.sendAndWait<Any>(createBasketCommand)
//        }
//    }
//
//  @Test
//    fun `ユーザーが存在しない_買い物かごを作成する_失敗`() {
//        // Arrange
//        val createBasketCommand = CreateBasketCommand(
//            id = BasketId(),
//            userId = UserId(),
//        )
//
//        // Act
//        val exception = assertThrows<CommandExecutionException> {
//            commandGateway.sendAndWait<Any>(createBasketCommand)
//        }
//
//        // Assert
//        assert(exception.isWrapUseCaseError())
//        assert(exception.getWrapUseCaseError().errorCode == CreateBasketError.USER_NOT_FOUND.errorCode)
//    }
//
//  @Test
//  fun `ユーザーが存在する_存在するユーザーで買い物かごを作成する_失敗`() {
//    // Arrange
//    // Userを作成するとSaga二より自動的に買い物かごが作成されるので、見かけ上３回買い物かごが作成されたように見える
//    val userId = userTestDataCreator.handle()
// //    val createBasketCommand1 = CreateBasketCommand(
// //      id = BasketId(),
// //      userId = userId,
// //    )
//    val createBasketCommand2 = CreateBasketCommand(
//      id = BasketId(),
//      userId = userId,
//    )
//
// //    commandGateway.sendAndWait<Any>(createBasketCommand1)
//
//   // Thread.sleep(100)
//    // Act
// //    val exception = assertThrows<CommandExecutionException> {
// //      commandGateway.sendAndWait<Any>(createBasketCommand2)
// //    }
//
//    commandGateway.sendAndWait<Any>(createBasketCommand2)
//
//    Thread.sleep(3000)
//
//    // Assert
// //    assert(exception.isWrapUseCaseError())
// //   assert(exception.getWrapUseCaseError().errorCode == CreateBasketError.BASKET_ALREADY_LINKED_TO_USER.errorCode)
//  }
// }
