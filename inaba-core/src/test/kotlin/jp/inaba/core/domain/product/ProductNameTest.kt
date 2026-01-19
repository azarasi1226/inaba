package jp.inaba.core.domain.product

import jp.inaba.core.domain.common.ValueObjectException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class ProductNameTest {
    @Test
    fun `空文字で例外`() {
        assertThrows<ValueObjectException> {
            ProductName("")
        }
    }

    @Test
    fun `1文字で成功`() {
        assertDoesNotThrow {
            ProductName("A")
        }
    }

    @Test
    fun `200文字で成功`() {
        assertDoesNotThrow {
            ProductName("A".repeat(200))
        }
    }

    @Test
    fun `201文字で例外`() {
        assertThrows<ValueObjectException> {
            ProductName("A".repeat(201))
        }
    }
}
