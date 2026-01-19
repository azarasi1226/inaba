package jp.inaba.core.domain.brand

import jp.inaba.core.domain.common.ValueObjectException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class BrandNameTest {
    @Test
    fun `正常な値でBrandName作成_成功`() {
        assertDoesNotThrow {
            BrandName("Apple")
        }
    }

    @ParameterizedTest
    @ValueSource(ints = [1, 300])
    fun `1 ~ 300文字の間でBrandName作成_成功`(count: Int) {
        assertDoesNotThrow {
            BrandName("A".repeat((count)))
        }
    }

    @Test
    fun `空文字でBrandName作成_例外`() {
        assertThrows<ValueObjectException> {
            BrandName("")
        }
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 301])
    fun `0文字または301文字以上でBrandName作成_例外`(count: Int) {
        assertThrows<ValueObjectException> {
            BrandName("A".repeat((count)))
        }
    }
}
