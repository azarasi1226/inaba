package jp.inaba.core.domain.common

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class PagingTest {
    @Test
    fun `totalCountが負の値_例外`() {
        assertThrows<ValueObjectException> {
            Paging(totalCount = -1, pageSize = 10, pageNumber = 1)
        }
    }

    @Test
    fun `pageSizeが0_例外`() {
        assertThrows<ValueObjectException> {
            Paging(totalCount = 100, pageSize = 0, pageNumber = 1)
        }
    }

    @Test
    fun `pageSizeが負の値_例外`() {
        assertThrows<ValueObjectException> {
            Paging(totalCount = 100, pageSize = -1, pageNumber = 1)
        }
    }

    @Test
    fun `pageNumberが0_例外`() {
        assertThrows<ValueObjectException> {
            Paging(totalCount = 100, pageSize = 10, pageNumber = 0)
        }
    }

    @Test
    fun `pageNumberが負の値_例外`() {
        assertThrows<ValueObjectException> {
            Paging(totalCount = 100, pageSize = 10, pageNumber = -1)
        }
    }

    @ParameterizedTest
    @CsvSource(
        // 割り切れる場合
        "100,10,1,10",
        // 割り切れない場合（繰り上げ）
        "105,10,1,11",
        // 1件の場合
        "1,10,1,1",
        // 0件の場合
        "0,10,1,0",
    )
    fun `totalPageが正しく計算される`(
        totalCount: Long,
        pageSize: Int,
        pageNumber: Int,
        expectedTotalPage: Int,
    ) {
        val paging = Paging(totalCount = totalCount, pageSize = pageSize, pageNumber = pageNumber)
        assertEquals(expectedTotalPage, paging.totalPage)
    }

    @Test
    fun `正常なページング情報で作成される`() {
        assertDoesNotThrow {
            Paging(totalCount = 100, pageSize = 20, pageNumber = 1)
        }
    }
}
