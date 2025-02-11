package jp.inaba.service.infrastructure.jpa.lookupstock

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LookupStockJpaRepository : JpaRepository<LookupStockJpaEntity, String> {
    fun existsByProductId(productId: String): Boolean
}
