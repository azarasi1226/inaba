package jp.inaba.service2.feature.brand

import jp.inaba.core.domain.brand.BrandId
import jp.inaba.core.domain.brand.BrandName
import jp.inaba.message.brand.command.CreateBrandCommand
import jp.inaba.message.brand.command.CreateBrandResult
import jp.inaba.message.brand.event.BrandCreatedEvent
import jp.inaba.service2.feature.InabaIntegrationTestBase
import org.junit.jupiter.api.Test

class CreateBrandTest : InabaIntegrationTestBase() {
    @Test
    fun `正常系`() {
        val command =
            CreateBrandCommand(
                id = BrandId(),
                name = BrandName("テストブランド"),
            )

        fixture
            .given()
            .noPriorActivity()
            .`when`()
            .command(command)
            .then()
            .resultMessagePayload(CreateBrandResult.success())
            .events(
                BrandCreatedEvent(
                    id = command.id.value,
                    name = command.name.value,
                ),
            )
    }

    @Test
    fun `すでに同じIDでユーザーが登録されている_duplicated`() {
        val command =
            CreateBrandCommand(
                id = BrandId(),
                name = BrandName("テストブランド"),
            )

        fixture
            .given()
            .events(
                BrandCreatedEvent(
                    id = command.id.value,
                    name = command.name.value,
                ),
            ).`when`()
            .command(command)
            .then()
            .resultMessagePayload(CreateBrandResult.duplicated())
    }
}
