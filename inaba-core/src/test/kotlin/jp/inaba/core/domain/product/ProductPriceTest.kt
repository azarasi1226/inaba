package jp.inaba.core.domain.product

import jp.inaba.core.domain.common.ValueObjectException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class ProductPriceTest {
    @Test
    fun `正常な価格で生成される`() {
        assertDoesNotThrow {
            val price = ProductPrice(1500)
            assertEquals(1500, price.value)
        }
    }

    @Test
    fun `最小値で生成される`() {
        assertDoesNotThrow {
            val price = ProductPrice(1)
            assertEquals(1, price.value)
        }
    }

    @Test
    fun `最大値超えで例外`() {
        assertThrows<ValueObjectException> {
            ProductPrice(1_000_000_001)
        }
    }

    @Test
    fun `0以下で例外`() {
        assertThrows<ValueObjectException> {
            ProductPrice(0)
        }
    }
}

