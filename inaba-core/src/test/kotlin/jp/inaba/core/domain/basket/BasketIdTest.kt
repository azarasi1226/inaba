import de.huxhorn.sulky.ulid.ULID
import jp.inaba.core.domain.basket.BasketId
import jp.inaba.core.domain.common.ValueObjectException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class BasketIdTest {
    @Test
    fun `不正な値でBasketId作成_例外`() {
        assertThrows<ValueObjectException> {
            BasketId("")
        }
    }

    @Test
    fun `正常な値でBasketId作成_成功`() {
        assertDoesNotThrow {
            BasketId(ULID().nextULID())
        }
    }

    @Test
    fun `デフォルトコンストラクタでBasketId作成_成功`() {
        assertDoesNotThrow {
            BasketId()
        }
    }
}
