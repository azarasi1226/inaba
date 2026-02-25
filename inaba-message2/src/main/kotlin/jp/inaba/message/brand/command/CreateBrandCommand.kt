package jp.inaba.message.brand.command

import jp.inaba.core.domain.brand.BrandId
import jp.inaba.core.domain.brand.BrandName
import jp.inaba.message.Error
import jp.inaba.message.UseCaseResult
import org.axonframework.modelling.annotation.TargetEntityId

data class CreateBrandCommand(
    @TargetEntityId
    val id: BrandId,
    val name: BrandName,
)

class CreateBrandResult private constructor(
    override val success: Boolean,
    override val error: Error?,
) : UseCaseResult() {
    companion object {
        fun success(): CreateBrandResult = CreateBrandResult(true, null)
        fun alreadyExists(): CreateBrandResult = CreateBrandResult(false, Error("ブランドが既に存在しています"))
    }
}
