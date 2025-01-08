package jp.inaba.service.infrastructure.jpa.lookupbasket

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LookupBasketJpaRepository : JpaRepository<LookupBasketJpaEntity, String> {
    fun existsByUserId(userId: String): Boolean
}
