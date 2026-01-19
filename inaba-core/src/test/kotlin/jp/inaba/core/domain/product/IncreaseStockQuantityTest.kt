package jp.inaba.core.domain.product

import jp.inaba.core.domain.common.ValueObjectException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class IncreaseStockQuantityTest {
    @Test
    fun `正常な増加量で生成される`() {
        assertDoesNotThrow {
            IncreaseStockQuantity(1)
        }
    }

    @Test
    fun `最大値で成功`() {
        assertDoesNotThrow {
            IncreaseStockQuantity(1_000_000)
        }
    }

    @Test
    fun `0や負や上限以上で例外`() {
        assertThrows<ValueObjectException> {
            IncreaseStockQuantity(0)
        }
        assertThrows<ValueObjectException> {
            IncreaseStockQuantity(-1)
        }
        assertThrows<ValueObjectException> {
            IncreaseStockQuantity(1_000_001)
        }
    }
}

