package jp.inaba.basket.service.infrastructure.jpa.basketitem

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface BasketItemJpaRepository : JpaRepository<BasketItemJpaEntity, String> {
    @Modifying
    @Transactional
    @Query("DELETE FROM BasketItemJpaEntity b WHERE b.basket.id = :basketId")
    fun deleteByBasketId(@Param("basketId") basketId: String)

    @Modifying
    @Transactional
    @Query("DELETE FROM BasketItemJpaEntity b WHERE b.basket.id = :basketId AND b.product.id = :productId")
    fun deleteByBasketIdAndProductId(@Param("basketId") basketId: String, @Param("productId") productId: String)
}