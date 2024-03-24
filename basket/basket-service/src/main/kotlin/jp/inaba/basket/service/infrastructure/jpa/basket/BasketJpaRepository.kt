package jp.inaba.basket.service.infrastructure.jpa.basket

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface BasketJpaRepository: JpaRepository<BasketJpaEntity, String> {
    fun findByUserId(userid: String): Optional<BasketJpaEntity>
    fun existsByUserId(userId: String): Boolean
}