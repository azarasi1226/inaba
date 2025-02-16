package jp.inaba.service.infrastructure.jpa.lookupbrand

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LookupBrandJpaRepository : JpaRepository<LookupBrandJpaEntity, String>