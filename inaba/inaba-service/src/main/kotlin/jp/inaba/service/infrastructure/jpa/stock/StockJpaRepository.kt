package jp.inaba.service.infrastructure.jpa.stock

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StockJpaRepository : JpaRepository<StockJpaEntity, String>
