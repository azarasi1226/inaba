package jp.inaba.basket.service.infrastructure.jpa.basket

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BasketJpaRepository: JpaRepository<BasketJpaEntity, String> {
    fun existsByUserId(userId: String): Boolean
}