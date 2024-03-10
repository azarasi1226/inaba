package jp.inaba.basket.service.infrastructure.jpa.item

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface  ItemJpaRepository : JpaRepository<ItemJpaEntity, String>