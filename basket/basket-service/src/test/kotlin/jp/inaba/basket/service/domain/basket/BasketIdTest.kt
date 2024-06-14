package jp.inaba.basket.service.domain.basket

import jp.inaba.basket.api.domain.basket.BasketId
import jp.inaba.common.domain.shared.DomainException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class BasketIdTest {
    @Test
    fun `不正な値でBasketId作成_例外`() {
        assertThrows<DomainException> {
            BasketId("")
        }
    }

//    @Test
//    fun `正常な値でBasketId作成_成功`() {
//        assertDoesNotThrow {
//            val userId = UserId()
//            BasketId("basket-$userId")
//        }
//    }
}
