package jp.inaba.service.domain.product

import jp.inaba.core.domain.brand.BrandId

interface CreateProductVerifier {
    fun isBrandNotFound(brandId: BrandId): Boolean
}
