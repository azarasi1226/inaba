package jp.inaba.service.domain.brand

import jp.inaba.core.domain.brand.BrandId
import jp.inaba.core.domain.brand.BrandName
import jp.inaba.message.brand.command.DeleteBrandCommand
import jp.inaba.message.brand.event.BrandCreatedEvent
import jp.inaba.message.brand.event.BrandDeletedEvent
import org.axonframework.test.aggregate.AggregateTestFixture
import org.axonframework.test.aggregate.FixtureConfiguration
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class BrandAggregateTest {
    private lateinit var fixture: FixtureConfiguration<BrandAggregate>

    @BeforeEach
    fun before() {
        fixture = AggregateTestFixture(BrandAggregate::class.java)
    }

    @Test
    fun `ブランドを作成_Event発行`() {
        // Arrange
        val brandId = BrandId()
        val brandName = BrandName("Apple")

        fixture
            .givenNoPriorActivity()
            // Act
            .`when`(
                InternalCreateBrandCommand(
                    id = brandId,
                    name = brandName,
                ),
            )
            // Assert
            .expectSuccessfulHandlerExecution()
            .expectEvents(
                BrandCreatedEvent(
                    id = brandId.value,
                    name = brandName.value,
                ),
            )
    }

    @Test
    fun `ブランドを削除_Event発行`() {
        // Arrange
        val brandId = BrandId()
        val brandName = BrandName("Apple")

        fixture
            .given(
                BrandCreatedEvent(
                    id = brandId.value,
                    name = brandName.value,
                ),
            )
            // Act
            .`when`(
                DeleteBrandCommand(
                    id = brandId,
                ),
            )
            // Assert
            .expectSuccessfulHandlerExecution()
            .expectEvents(
                BrandDeletedEvent(
                    id = brandId.value,
                ),
            )
    }
}

