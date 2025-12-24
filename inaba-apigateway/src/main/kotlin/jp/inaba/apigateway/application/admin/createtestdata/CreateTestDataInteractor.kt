package jp.inaba.apigateway.application.admin.createtestdata

import org.springframework.stereotype.Service

@Service
class CreateTestDataInteractor(
    private val brandTestDataCreator: BrandTestDataCreator,
    private val productTestDataCreator: ProductTestDataCreator,
) {
    fun handle() {
        val brandId = brandTestDataCreator.handle()
        productTestDataCreator.handle(brandId)
    }
}
