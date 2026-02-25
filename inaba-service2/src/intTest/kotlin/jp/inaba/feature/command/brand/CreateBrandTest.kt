package jp.inaba.feature.command.brand

import jp.inaba.InabaIntegrationTestBase
import jp.inaba.core.domain.brand.BrandId
import jp.inaba.core.domain.brand.BrandName
import jp.inaba.message.brand.command.CreateBrandCommand
import jp.inaba.message.brand.command.CreateBrandResult
import jp.inaba.message.brand.event.BrandCreatedEvent
import org.junit.jupiter.api.Test

class CreateBrandTest : InabaIntegrationTestBase() {
  @Test
  fun `正常系`(){
    val command = CreateBrandCommand(
      id = BrandId(),
      name = BrandName("テストブランド"),
    )

    fixture.given()
      .noPriorActivity()
      .`when`()
      .command(command)
      .then()
      .success()
      .events(
        BrandCreatedEvent(
          id = command.id.value,
          name = command.name.value,
        )
      )
  }

  @Test
  fun `すでに同じIDでユーザーが登録されている_エラー`() {
    val command = CreateBrandCommand(
      id = BrandId(),
      name = BrandName("テストブランド"),
    )

    fixture.given()
      .events(
        BrandCreatedEvent(
          id = command.id.value,
          name = command.name.value,
        )
      )
      .`when`()
      .command(command)
      .then()
      .resultMessagePayloadSatisfies(
        CreateBrandResult::class.java
      ){ result ->
          result.error!!.message == CreateBrandResult.alreadyExists().error!!.message
      }
  }
}