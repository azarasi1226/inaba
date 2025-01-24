package jp.inaba.service.infrastructure.jpa.product

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ProductJpaRepository : JpaRepository<ProductJpaEntity, String> {
    fun findByStockId(id: String): Optional<ProductJpaEntity>
}
