package jp.inaba.service.infrastructure.domain.product

import jp.inaba.core.domain.brand.BrandId
import jp.inaba.service.domain.product.CreateProductVerifier
import jp.inaba.service.infrastructure.jpa.brand.BrandJpaRepository
import org.springframework.stereotype.Service

@Service
class CreateProductVerifierImpl(
    private val brandJpaRepository: BrandJpaRepository,
) : CreateProductVerifier {
    override fun isBrandNotFound(brandId: BrandId): Boolean = brandJpaRepository.findById(brandId.value).isEmpty
}
