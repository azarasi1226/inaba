package jp.inaba.service.application.command.brand

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import jp.inaba.core.domain.brand.BrandId
import jp.inaba.core.domain.brand.BrandName
import jp.inaba.core.domain.common.CommonError
import jp.inaba.core.domain.common.UseCaseException
import jp.inaba.message.brand.command.CreateBrandCommand
import jp.inaba.service.domain.UniqueAggregateIdVerifier
import org.axonframework.commandhandling.gateway.CommandGateway
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class CreateBrandInteractorTest {
    @MockK
    private lateinit var uniqueAggregateIdVerifier: UniqueAggregateIdVerifier

    @MockK
    private lateinit var commandGateway: CommandGateway

    @InjectMockKs
    private lateinit var sut: CreateBrandInteractor

    @AfterEach
    fun after() {
        confirmVerified(commandGateway)
    }

    @Test
    fun `ブランドを作成_Commandが発行される`() {
        // Arrange
        val brandId = BrandId()
        val brandName = BrandName("Apple")
        val command = CreateBrandCommand(id = brandId, name = brandName)
        every {
            uniqueAggregateIdVerifier.hasDuplicateAggregateId(brandId.value)
        } returns false
        every {
            commandGateway.sendAndWait<Any>(any())
        } returns Unit

        // Act
        sut.handle(command)

        // Assert
        verify(exactly = 1) {
            commandGateway.sendAndWait<Any>(any())
        }
    }

    @Test
    fun `登録しようとしているIDで集約が作成済み_ブランドを作成_Commandが発行されずException`() {
        // Arrange
        val brandId = BrandId()
        val brandName = BrandName("Apple")
        val command = CreateBrandCommand(id = brandId, name = brandName)
        every {
            uniqueAggregateIdVerifier.hasDuplicateAggregateId(brandId.value)
        } returns true

        // Act
        val exception = assertThrows<UseCaseException> {
            sut.handle(command)
        }

        // Assert
        assert(exception.error == CommonError.AGGREGATE_DUPLICATED)
    }
}

