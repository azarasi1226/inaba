package jp.inaba.service.infrastructure.domain.product

import jp.inaba.core.domain.brand.BrandId
import jp.inaba.service.domain.product.CreateProductVerifier
import jp.inaba.service.infrastructure.jooq.generated.tables.references.BRAND
import org.jooq.DSLContext
import org.springframework.stereotype.Service

@Service
class CreateProductVerifierImpl(
    private val dsl: DSLContext,
) : CreateProductVerifier {
    override fun isBrandNotFound(brandId: BrandId): Boolean =
        !dsl.fetchExists(
            dsl.selectOne().from(BRAND).where(BRAND.ID.eq(brandId.value)),
        )
}
