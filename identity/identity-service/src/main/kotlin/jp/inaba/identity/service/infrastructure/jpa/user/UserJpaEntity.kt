package jp.inaba.identity.service.infrastructure.jpa.user

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

// TODO("userテーブルだとH2でテーブル作れないんやけど....")
@Entity
@Table(name = "users")
data class UserJpaEntity(
    @Id
    var id: String = "",
    var userName: String = "",
)
