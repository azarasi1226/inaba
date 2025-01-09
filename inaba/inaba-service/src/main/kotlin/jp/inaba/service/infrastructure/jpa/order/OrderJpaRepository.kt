package jp.inaba.service.infrastructure.jpa.order

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderJpaRepository : JpaRepository<OrderEntity, String> {
    fun findByUserId(userId: String): List<OrderEntity>
}
