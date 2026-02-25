package jp.inaba.message.brand.command

import jp.inaba.core.domain.brand.BrandId
import jp.inaba.message.Error
import jp.inaba.message.UseCaseResult
import org.axonframework.modelling.annotation.TargetEntityId

data class DeleteBrandCommand(
    @TargetEntityId
    val id: BrandId,
)

class DeleteBrandResult private constructor(
    override val success: Boolean,
    override val error: Error?,
) : UseCaseResult() {
    companion object {
        fun success(): DeleteBrandResult = DeleteBrandResult(true, null)
        fun brandNotFound(): DeleteBrandResult = DeleteBrandResult(false, Error("ブランドが存在しません"))
    }
}
