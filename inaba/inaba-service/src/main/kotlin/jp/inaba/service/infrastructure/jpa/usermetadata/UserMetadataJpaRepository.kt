package jp.inaba.service.infrastructure.jpa.usermetadata

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserMetadataJpaRepository : JpaRepository<UserMetadataJpaEntity, String> {
    fun findByUserId(id: String): Optional<UserMetadataJpaEntity>
}