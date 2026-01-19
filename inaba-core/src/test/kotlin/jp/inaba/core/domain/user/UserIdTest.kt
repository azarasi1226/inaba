package jp.inaba.core.domain.user

import de.huxhorn.sulky.ulid.ULID
import jp.inaba.core.domain.common.ValueObjectException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class UserIdTest {
    @Test
    fun `不正な値でUserId作成_例外`() {
        assertThrows<ValueObjectException> {
            UserId("")
        }
    }

    @Test
    fun `正常な値でUserId作成_成功`() {
        assertDoesNotThrow {
            UserId(ULID().nextULID())
        }
    }

    @Test
    fun `デフォルトコンストラクタでUserId作成_成功`() {
        assertDoesNotThrow {
            UserId()
        }
    }
}
