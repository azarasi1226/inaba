package jp.inaba.service.domain.brand

import jp.inaba.core.domain.brand.BrandId

interface CreateBrandVerifier {
    fun isBrandExits(brandId: BrandId): Boolean
}