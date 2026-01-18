package jp.inaba.core

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName

@DisplayName("JUnit 5 テスト動作確認")
class JunitSampleTest {

    @Test
    @DisplayName("足し算が正しく計算される")
    fun `1 + 1 = 2`() {
        // Arrange
        val a = 1
        val b = 1

        // Act
        val result = a + b

        // Assert
    }

    @Test
    @DisplayName("文字列が正しく比較される")
    fun `文字列比較テスト`() {
        // Arrange
        val actual = "Hello"

        // Act & Assert
    }
}

