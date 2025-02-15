package jp.inaba.service.infrastructure.jpa.brand

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BrandJpaRepository : JpaRepository<BrandJpaEntity, String>
