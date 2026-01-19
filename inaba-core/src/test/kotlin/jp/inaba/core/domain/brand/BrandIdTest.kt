package jp.inaba.core.domain.brand

import de.huxhorn.sulky.ulid.ULID
import jp.inaba.core.domain.common.ValueObjectException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class BrandIdTest {
    @Test
    fun `不正な値でBrandId作成_例外`() {
        assertThrows<ValueObjectException> {
            BrandId("")
        }
    }

    @Test
    fun `正常な値でBrandId作成_成功`() {
        assertDoesNotThrow {
            BrandId(ULID().nextULID())
        }
    }

    @Test
    fun `デフォルトコンストラクタでBrandId作成_成功`() {
        assertDoesNotThrow {
            BrandId()
        }
    }
}
