package jp.inaba.core.domain.order

import de.huxhorn.sulky.ulid.ULID
import jp.inaba.core.domain.common.ValueObjectException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class OrderIdTest {
    @Test
    fun `不正な値でOrderId作成_例外`() {
        assertThrows<ValueObjectException> {
            OrderId("")
        }
    }

    @Test
    fun `正常な値でOrderId作成_成功`() {
        assertDoesNotThrow {
            OrderId(ULID().nextULID())
        }
    }

    @Test
    fun `デフォルトコンストラクタでOrderId作成_成功`() {
        assertDoesNotThrow {
            OrderId()
        }
    }
}