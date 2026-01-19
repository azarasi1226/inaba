package jp.inaba.service.domain.order

import org.junit.jupiter.api.Test

class OrderStatusTest {
    @Test
    fun `Issued_値が1`() {
        // Act & Assert
        assert(OrderStatus.Issued.value == 1)
    }

    @Test
    fun `Completed_値が2`() {
        // Act & Assert
        assert(OrderStatus.Completed.value == 2)
    }

    @Test
    fun `Failed_値が3`() {
        // Act & Assert
        assert(OrderStatus.Failed.value == 3)
    }
}
