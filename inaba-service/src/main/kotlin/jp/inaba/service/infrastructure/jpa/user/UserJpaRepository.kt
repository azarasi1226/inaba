package jp.inaba.service.infrastructure.jpa.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserJpaRepository : JpaRepository<UserJpaEntity, String>
