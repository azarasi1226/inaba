package jp.inaba.core.domain.common

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class PagingConditionTest {
    @Test
    fun `pageSizeが0_例外`() {
        assertThrows<ValueObjectException> {
            PagingCondition(pageSize = 0, pageNumber = 1)
        }
    }

    @Test
    fun `pageSizeが負の値_例外`() {
        assertThrows<ValueObjectException> {
            PagingCondition(pageSize = -1, pageNumber = 1)
        }
    }

    @Test
    fun `pageNumberが0_例外`() {
        assertThrows<ValueObjectException> {
            PagingCondition(pageSize = 10, pageNumber = 0)
        }
    }

    @Test
    fun `pageNumberが負の値_例外`() {
        assertThrows<ValueObjectException> {
            PagingCondition(pageSize = 10, pageNumber = -1)
        }
    }

    @ParameterizedTest
    @CsvSource(
        // 1ページ目, offset=0
        "10,1,0",
        // 2ページ目, offset=10
        "10,2,10",
        // 5ページ目, offset=80
        "20,5,80",
    )
    fun `offsetが正しく計算される`(
        pageSize: Int,
        pageNumber: Int,
        expectedOffset: Int,
    ) {
        val condition = PagingCondition(pageSize = pageSize, pageNumber = pageNumber)
        assertEquals(expectedOffset, condition.offset)
    }

    @Test
    fun `正常なページング条件で作成される`() {
        assertDoesNotThrow {
            PagingCondition(pageSize = 20, pageNumber = 1)
        }
    }
}
