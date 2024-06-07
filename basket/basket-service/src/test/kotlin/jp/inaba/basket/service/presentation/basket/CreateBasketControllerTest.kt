package jp.inaba.basket.service.presentation.basket

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockkStatic
import jp.inaba.basket.api.domain.basket.CreateBasketError
import jp.inaba.basket.api.domain.basket.createBasket
import jp.inaba.basket.service.presentation.basket.create.CreateBasketController
import jp.inaba.basket.service.presentation.basket.create.CreateBasketRequest
import jp.inaba.common.presentation.shared.ErrorResponse
import jp.inaba.identity.api.domain.user.UserId
import org.axonframework.commandhandling.gateway.CommandGateway
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(CreateBasketController::class)
class CreateBasketControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean
    private lateinit var commandGateway: CommandGateway

    @Test
    fun `ユーザーが存在する_買い物かごを作成する_成功する`() {
        val userId = UserId()
        val request = CreateBasketRequest(userId.value)

        mockkStatic(CommandGateway::createBasket)
        every {
            commandGateway.createBasket(any())
        } returns Ok(Unit)

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/api/baskets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)),
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
    }

    @Test
    fun `ユーザーが存在しない_買い物かごを作成する_失敗する`() {
        val userId = UserId()
        val request = CreateBasketRequest(userId.value)

        mockkStatic(CommandGateway::createBasket)
        every {
            commandGateway.createBasket(any())
        } returns Err(CreateBasketError.USER_NOT_FOUND)

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/api/baskets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)),
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(
                MockMvcResultMatchers.content().json(
                    objectMapper.writeValueAsString(ErrorResponse(CreateBasketError.USER_NOT_FOUND)),
                ),
            )
    }
}
