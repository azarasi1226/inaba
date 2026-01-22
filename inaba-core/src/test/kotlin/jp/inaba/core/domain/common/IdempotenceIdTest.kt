package jp.inaba.core.domain.common

import de.huxhorn.sulky.ulid.ULID
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class IdempotenceIdTest {
    @Test
    fun `不正な値でIdempotencyId作成_例外`() {
        assertThrows<ValueObjectException> {
            IdempotencyId("")
        }
    }

    @Test
    fun `正常な値でIdempotencyId作成_成功`() {
        assertDoesNotThrow {
            IdempotencyId(ULID().nextULID())
        }
    }

    @Test
    fun `デフォルトコンストラクタでIdempotencyId作成_成功`() {
        assertDoesNotThrow {
            IdempotencyId()
        }
    }
}
