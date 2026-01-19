package jp.inaba.core.domain.product

import de.huxhorn.sulky.ulid.ULID
import jp.inaba.core.domain.common.ValueObjectException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class ProductIdTest {
    @Test
    fun `不正な値でProductId作成_例外`() {
        assertThrows<ValueObjectException> {
            ProductId("")
        }
    }

    @Test
    fun `正常な値でProductId作成_成功`() {
        assertDoesNotThrow {
            ProductId(ULID().nextULID())
        }
    }

    @Test
    fun `デフォルトコンストラクタでProductId作成_成功`() {
        assertDoesNotThrow {
            ProductId()
        }
    }
}

