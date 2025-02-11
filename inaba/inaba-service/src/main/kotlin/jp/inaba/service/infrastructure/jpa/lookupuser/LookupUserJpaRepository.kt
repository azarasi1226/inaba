package jp.inaba.service.infrastructure.jpa.lookupuser

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LookupUserJpaRepository : JpaRepository<LookupUserJpaEntity, String> {
    fun existsBySubject(subject: String): Boolean
}
