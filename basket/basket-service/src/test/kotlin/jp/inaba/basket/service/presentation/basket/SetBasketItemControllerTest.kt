package jp.inaba.basket.service.presentation.basket

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockkStatic
import jp.inaba.basket.api.domain.basket.setBasketItem
import jp.inaba.basket.service.presentation.basket.setbasketitem.SetBasketItemController
import jp.inaba.basket.service.presentation.basket.setbasketitem.SetBasketItemRequest
import jp.inaba.basket.share.domain.basket.SetBasketItemError
import jp.inaba.catalog.api.domain.product.ProductId
import jp.inaba.common.presentation.shared.ErrorResponse
import jp.inaba.identity.share.domain.user.UserId
import org.axonframework.commandhandling.gateway.CommandGateway
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(SetBasketItemController::class)
class SetBasketItemControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean
    private lateinit var commandGateway: CommandGateway

    @Test
    fun `買い物かごの中身にアイテムが0~49種類入っている_買い物かごにアイテムを入れる_成功`() {
        val userId = UserId()
        val productId = ProductId()
        val quantity = 5
        val request =
            SetBasketItemRequest(
                productId = productId.value,
                itemQuantity = quantity,
            )

        mockkStatic(CommandGateway::setBasketItem)
        every {
            commandGateway.setBasketItem(any())
        } returns Ok(Unit)

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/api/baskets/${userId.value}/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)),
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `存在しないアイテムを買い物かごに追加する_失敗`() {
        val userId = UserId()
        val productId = ProductId()
        val quantity = 5
        val request =
            SetBasketItemRequest(
                productId = productId.value,
                itemQuantity = quantity,
            )

        mockkStatic(CommandGateway::setBasketItem)
        every {
            commandGateway.setBasketItem(any())
        } returns Err(SetBasketItemError.PRODUCT_NOT_FOUND)

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/api/baskets/${userId.value}/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)),
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(
                MockMvcResultMatchers.content().json(
                    objectMapper.writeValueAsString(ErrorResponse(SetBasketItemError.PRODUCT_NOT_FOUND)),
                ),
            )
    }

    @Test
    fun `買い物かごの中身にアイテムが50種類入っている_買い物かごにアイテムを入れる_失敗`() {
        val userId = UserId()
        val productId = ProductId()
        val quantity = 5
        val request =
            SetBasketItemRequest(
                productId = productId.value,
                itemQuantity = quantity,
            )

        mockkStatic(CommandGateway::setBasketItem)
        every {
            commandGateway.setBasketItem(any())
        } returns Err(SetBasketItemError.PRODUCT_MAX_KIND_OVER)

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/api/baskets/${userId.value}/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)),
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(
                MockMvcResultMatchers.content().json(
                    objectMapper.writeValueAsString(ErrorResponse(SetBasketItemError.PRODUCT_MAX_KIND_OVER)),
                ),
            )
    }
}
