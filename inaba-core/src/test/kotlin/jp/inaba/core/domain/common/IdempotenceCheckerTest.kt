package jp.inaba.core.domain.common

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class IdempotenceCheckerTest {
    @Test
    fun `初回チェック_idempotentではない`() {
        // Arrange
        val sut = IdempotenceChecker()
        val idempotencyId = IdempotencyId()

        // Act
        val actual = sut.isIdempotent(idempotencyId)

        // Assert
        assertFalse(actual)
    }

    @Test
    fun `登録後のチェック_idempotentになる`() {
        // Arrange
        val sut = IdempotenceChecker()
        val idempotencyId = IdempotencyId()
        sut.register(idempotencyId)

        // Act
        val actual = sut.isIdempotent(idempotencyId)

        // Assert
        assertTrue(actual)
    }

    @Test
    fun `100件を超えると古いIDが削除される`() {
        // Arrange
        val sut = IdempotenceChecker()
        val first = IdempotencyId()
        val second = IdempotencyId()
        sut.register(first)
        sut.register(second)

        // 101件目まで登録（最初のIDが削除される）
        repeat(99) {
            sut.register(IdempotencyId())
        }

        // Act & Assert
        assertFalse(sut.isIdempotent(first), "最初に登録したIDは削除されているはず")
        assertTrue(sut.isIdempotent(second), "2番目に登録したIDはぎり残ってるはず")
    }

    @Test
    fun `同じIDを複数回登録しても問題ない`() {
        // Arrange
        val sut = IdempotenceChecker()
        val idempotencyId = IdempotencyId()

        // Act
        sut.register(idempotencyId)
        sut.register(idempotencyId)
        sut.register(idempotencyId)

        // Assert
        assertTrue(sut.isIdempotent(idempotencyId))
    }
}
