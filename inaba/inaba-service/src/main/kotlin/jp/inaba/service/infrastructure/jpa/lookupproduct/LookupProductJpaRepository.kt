package jp.inaba.service.infrastructure.jpa.lookupproduct

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LookupProductJpaRepository : JpaRepository<LookupProductJpaEntity, String>