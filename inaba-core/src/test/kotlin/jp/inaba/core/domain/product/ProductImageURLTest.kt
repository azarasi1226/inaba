package jp.inaba.core.domain.product

import jp.inaba.core.domain.common.ValueObjectException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class ProductImageURLTest {
    @Test
    fun `nullは許容される`() {
        assertDoesNotThrow {
            ProductImageURL(null)
        }
    }

    @Test
    fun `正しいURLで成功`() {
        assertDoesNotThrow {
            ProductImageURL("http://example.com/image.png")
        }
    }

    @Test
    fun `不正なURLで例外`() {
        assertThrows<ValueObjectException> {
            ProductImageURL("not-a-url")
        }
    }
}

