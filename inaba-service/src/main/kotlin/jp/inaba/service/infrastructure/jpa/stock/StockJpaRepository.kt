package jp.inaba.service.infrastructure.jpa.stock

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface StockJpaRepository : JpaRepository<StockJpaEntity, String> {
    fun findByProductId(productId: String): Optional<StockJpaEntity>
}
