//package jp.inaba.basket.service.application.basket
//
//import jp.inaba.basket.api.domain.basket.BasketCommands
//import jp.inaba.basket.api.domain.basket.BasketId
//import jp.inaba.basket.service.application.command.basket.create.UserDuplicatedException
//import jp.inaba.basket.service.infrastructure.jpa.basket.BasketJpaRepository
//import org.axonframework.commandhandling.gateway.CommandGateway
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.assertDoesNotThrow
//import org.junit.jupiter.api.assertThrows
//import org.mockito.Mock
//import org.mockito.Mockito.mock
//import org.mockito.Mockito.`when`
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//
//@SpringBootTest
//class CreateBasketCommandInterceptorTest {
//    private lateinit var interceptor: CreateBasketCommandInterceptor
//
//    @Mock
//    private lateinit var repository: BasketJpaRepository
//
//    @Autowired
//    private lateinit var commandGateway: CommandGateway
//
//    @BeforeEach
//    fun before() {
//        repository = mock(BasketJpaRepository::class.java)
//        interceptor = CreateBasketCommandInterceptor(repository)
//        commandGateway.registerDispatchInterceptor(interceptor)
//    }
//
//    @Test
//    fun 登録しようとしているユーザーIDを持つ買い物かごが存在しない_買い物かごを作成する_成功() {
//        val userId = "seal1226"
//        `when`(repository.existsByUserId(userId))
//            .thenReturn(false)
//        val command = BasketCommands.Create(id = BasketId(), userId = userId)
//
//        assertDoesNotThrow {
//            commandGateway.sendAndWait<Any>(command)
//        }
//    }
//
//    @Test
//    fun ユーザーIDを含んだ買い物かごが存在する_買い物かごを作成する_例外() {
//        val userId = "seal1226"
//        `when`(repository.existsByUserId(userId))
//            .thenReturn(true)
//        val command = BasketCommands.Create(id = BasketId(), userId = userId)
//
//        assertThrows<UserDuplicatedException> {
//            commandGateway.sendAndWait<Any>(command)
//        }
//    }
//}