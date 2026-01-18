package jp.inaba.core.domain.common

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class PagingTest {
    private val expectedTotalCount = 100L
    private val expectedPageSize = 10
    private val expectedPageNumber = 1

    @Test
    fun `すべての値が正常_成功`() {
        assertDoesNotThrow {
            Paging(totalCount = expectedTotalCount, pageSize = expectedPageSize, pageNumber = expectedPageNumber)
        }
    }

    @ParameterizedTest
    @ValueSource(longs = [-1])
    fun `totalCountが不正な値_例外`(invalidTotalCount: Long) {
        assertThrows<ValueObjectException> {
            Paging(totalCount = invalidTotalCount, pageSize = expectedPageSize, pageNumber = expectedPageNumber)
        }
    }

    @ParameterizedTest
    @ValueSource(ints = [0, -1])
    fun `pageSizeが不正_例外`(invalidPageSize: Int) {
        assertThrows<ValueObjectException> {
            Paging(totalCount = expectedTotalCount, pageSize = invalidPageSize, pageNumber = expectedPageNumber)
        }
    }

    @ParameterizedTest
    @ValueSource(ints = [0, -1])
    fun `pageNumberが不正_例外`(invalidPageNumber: Int) {
        assertThrows<ValueObjectException> {
            Paging(totalCount = expectedTotalCount, pageSize = expectedPageSize, pageNumber = invalidPageNumber)
        }
    }

    @Test
    fun `totalPageが割り切れる場合_成功`() {
        val paging = Paging(totalCount = 100, pageSize = 10, pageNumber = expectedPageNumber)
        assertEquals(10, paging.totalPage)
    }

    @Test
    fun `totalPageが割り切れない場合_1繰り上がる`() {
        val paging = Paging(totalCount = 105, pageSize = 10, pageNumber = expectedPageNumber)
        assertEquals(11, paging.totalPage)
    }

    @Test
    fun `totalPageが正しく計算される`() {
        val paging = Paging(totalCount = 1, pageSize = 10, pageNumber = expectedPageNumber)
        assertEquals(1, paging.totalPage)
    }
}

