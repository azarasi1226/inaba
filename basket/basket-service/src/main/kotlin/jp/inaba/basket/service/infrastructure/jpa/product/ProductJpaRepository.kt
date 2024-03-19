package jp.inaba.basket.service.infrastructure.jpa.product

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface  ProductJpaRepository : JpaRepository<ProductJpaEntity, String>