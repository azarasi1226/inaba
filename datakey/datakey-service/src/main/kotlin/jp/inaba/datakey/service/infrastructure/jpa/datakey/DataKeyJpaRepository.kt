package jp.inaba.datakey.service.jp.inaba.datakey.service.infrastructure.jpa.datakey

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DataKeyJpaRepository : JpaRepository<DataKeyJpaEntity, String>
