package jp.inaba.core.domain.product

import jp.inaba.core.domain.common.ValueObjectException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class ProductDescriptionTest {
    @Test
    fun `空文字で例外`() {
        assertThrows<ValueObjectException> {
            ProductDescription("")
        }
    }

    @Test
    fun `1文字で成功`() {
        assertDoesNotThrow {
            ProductDescription("A")
        }
    }

    @Test
    fun `2000文字で成功`() {
        assertDoesNotThrow {
            ProductDescription("A".repeat(2000))
        }
    }

    @Test
    fun `2001文字で例外`() {
        assertThrows<ValueObjectException> {
            ProductDescription("A".repeat(2001))
        }
    }
}
