package jp.inaba.core.domain.product

import jp.inaba.core.domain.common.ValueObjectException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class StockQuantityTest {
    @Test
    fun `正常な在庫数で生成される`() {
        assertDoesNotThrow {
            val quantity = StockQuantity(100)
            assertEquals(100, quantity.value)
        }
    }

    @Test
    fun `最小値0で生成される`() {
        assertDoesNotThrow {
            val quantity = StockQuantity(0)
            assertEquals(0, quantity.value)
        }
    }

    @Test
    fun `上限超えで例外`() {
        assertThrows<ValueObjectException> {
            StockQuantity(1_000_001)
        }
    }

    @Test
    fun `在庫を追加できる`() {
        val quantity = StockQuantity(100)
        val result = quantity.add(IncreaseStockQuantity(50))
        assertEquals(150, result.value)
    }

    @Test
    fun `在庫を減らせる`() {
        val quantity = StockQuantity(100)
        val result = quantity.subtract(DecreaseStockQuantity(30))
        assertEquals(70, result.value)
    }

    @Test
    fun `在庫上限を超えて追加すると例外`() {
        val quantity = StockQuantity(1_000_000)
        assertThrows<ValueObjectException> {
            quantity.add(IncreaseStockQuantity(1))
        }
    }

    @Test
    fun `在庫を0未満にすると例外`() {
        val quantity = StockQuantity(0)
        assertThrows<ValueObjectException> {
            quantity.subtract(DecreaseStockQuantity(1))
        }
    }
}
