package jp.inaba.service.domain.basket

import jp.inaba.core.domain.basket.BasketItemQuantity
import jp.inaba.core.domain.common.ValueObjectException
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class BasketItemQuantityTest {
    @ParameterizedTest
    @ValueSource(ints = [0, 100])
    fun `不正な値でBasketItemQuantity生成_例外`(value: Int) {
        assertThrows<ValueObjectException> {
            BasketItemQuantity(value)
        }
    }

    @ParameterizedTest
    @ValueSource(ints = [1, 99])
    fun `正常な値でBasketItemQuantity作成_インスタンス化成功`(value: Int) {
        assertDoesNotThrow {
            BasketItemQuantity(value)
        }
    }
}
