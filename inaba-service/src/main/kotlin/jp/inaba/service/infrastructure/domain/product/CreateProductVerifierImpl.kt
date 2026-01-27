package jp.inaba.service.infrastructure.domain.product

import jp.inaba.core.domain.brand.BrandId
import jp.inaba.service.domain.brand.BrandAggregate
import jp.inaba.service.domain.product.CreateProductVerifier
import org.axonframework.modelling.command.Repository
import org.springframework.stereotype.Service

@Service
class CreateProductVerifierImpl(
    private val brandRepository: Repository<BrandAggregate>,
) : CreateProductVerifier {
    override fun isBrandNotFound(brandId: BrandId): Boolean =
        try {
            brandRepository.load(brandId.value)
            true
        } catch (_: Exception) {
            false
        }
}
