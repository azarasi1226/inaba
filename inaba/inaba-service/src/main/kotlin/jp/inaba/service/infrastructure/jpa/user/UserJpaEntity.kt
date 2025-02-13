package jp.inaba.service.infrastructure.jpa.user

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "user")
data class UserJpaEntity(
    @Id
    val id: String = "",
    val userName: String = "",
)
