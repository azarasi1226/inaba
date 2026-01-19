package jp.inaba.core.domain.product

import jp.inaba.core.domain.common.ValueObjectException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class DecreaseStockQuantityTest {
    @Test
    fun `正常な減少量で生成される`() {
        assertDoesNotThrow {
            DecreaseStockQuantity(1)
        }
    }

    @Test
    fun `最大値で成功`() {
        assertDoesNotThrow {
            DecreaseStockQuantity(1_000_000)
        }
    }

    @Test
    fun `0や負や上限以上で例外`() {
        assertThrows<ValueObjectException> {
            DecreaseStockQuantity(0)
        }
        assertThrows<ValueObjectException> {
            DecreaseStockQuantity(-1)
        }
        assertThrows<ValueObjectException> {
            DecreaseStockQuantity(1_000_001)
        }
    }
}
