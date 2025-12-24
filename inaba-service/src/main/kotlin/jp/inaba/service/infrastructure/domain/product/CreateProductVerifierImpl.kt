package jp.inaba.service.infrastructure.domain.product

import jp.inaba.core.domain.brand.BrandId
import jp.inaba.service.domain.product.CreateProductVerifier
import jp.inaba.service.infrastructure.jpa.lookupbrand.LookupBrandJpaRepository
import org.springframework.stereotype.Service

@Service
class CreateProductVerifierImpl(
    private val lookupBrandJpaRepository: LookupBrandJpaRepository,
) : CreateProductVerifier {
    override fun isBrandNotFound(brandId: BrandId): Boolean = lookupBrandJpaRepository.findById(brandId.value).isEmpty
}
